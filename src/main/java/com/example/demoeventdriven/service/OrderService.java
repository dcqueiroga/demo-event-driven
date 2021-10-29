package com.example.demoeventdriven.service;

import com.example.demoeventdriven.domain.Order;
import com.example.demoeventdriven.domain.OrderStatus;
import com.example.demoeventdriven.domain.dto.OrderDTO;
import com.example.demoeventdriven.exception.OrderFailedException;
import com.example.demoeventdriven.exception.OrderNotFoundException;
import com.example.demoeventdriven.messaging.OrderBinder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@EnableBinding(value = { OrderBinder.class })
public class OrderService {

    Map<UUID, Order> orderDataBase = new HashMap<>();

    @Autowired
    private OrderBinder orderBinder;

    public Order placeOrder(OrderDTO orderDTO) {
        log.info("Place orderDTO: {}", orderDTO);
        var order = Order.builder()
                .itemName(orderDTO.getItemName())
                .id(UUID.randomUUID())
                .status(OrderStatus.PENDING)
                .build();

        orderDataBase.put(order.getId(), order);

        orderBinder.inventoryCheckingOut()
                .send(MessageBuilder.withPayload(order)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build());

        log.info("Order placed: {}", order);

        return order;
    }

    @StreamListener(OrderBinder.INVENTORY_CHECKING_IN)
    public void checkInventory(@Payload Order order) throws InterruptedException, OrderFailedException {
        log.info("checkInventory order: {}", order);
        order.setStatus(OrderStatus.INVENTORY_CHECKING);

        orderDataBase.put(order.getId(), order);

        Thread.sleep(5_000);

        if (System.currentTimeMillis() % 2 == 0) {
            order.setStatus(OrderStatus.OUT_OF_STOCK);

            orderDataBase.put(order.getId(), order);

            log.info("Let's assume we ran out of stock for item: {}", order.getItemName());
            Thread.sleep(5_000);
            throw new OrderFailedException(String.format("Insufficient inventory for order: %s", order.getId()));
        }

        orderBinder.shippingOut()
                .send(MessageBuilder.withPayload(order)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build());
    }

    @StreamListener(OrderBinder.SHIPPING_IN)
    public void shipOrder(@Payload Order order) {
        log.info("Ship order: {}", order);
        order.setStatus(OrderStatus.SHIPPED);

        orderDataBase.put(order.getId(), order);

        log.info("ItemID: {} has been shipped", order.getId());
    }

    @StreamListener(OrderBinder.ORDER_DLQ)
    public void cancelOrder(@Payload Order order) {
        log.info("Cancel order: {}", order);
        order.setStatus(OrderStatus.CANCELED);

        orderDataBase.put(order.getId(), order);
    }

    public OrderStatus statusCheck(UUID orderUuid) {
        return Optional.ofNullable(orderDataBase.get(orderUuid))
                .orElseThrow(() -> new OrderNotFoundException("Order not found")).getStatus();
    }
}
