package com.example.demoeventdriven.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface OrderBinder {

    String INVENTORY_CHECKING_IN = "inventory-checking-in";
    String INVENTORY_CHECKING_OUT = "inventory-checking-out";
    String SHIPPING_IN = "shipping-in";
    String SHIPPING_OUT = "shipping-out";
    String ORDER_DLQ = "order-dlq";

    @Input(INVENTORY_CHECKING_IN)
    SubscribableChannel inventoryCheckingIn();

    @Output(INVENTORY_CHECKING_OUT)
    SubscribableChannel inventoryCheckingOut();

    @Input(SHIPPING_IN)
    SubscribableChannel shippingIn();

    @Output(SHIPPING_OUT)
    SubscribableChannel shippingOut();

    @Input(ORDER_DLQ)
    SubscribableChannel orderDlqIn();
}
