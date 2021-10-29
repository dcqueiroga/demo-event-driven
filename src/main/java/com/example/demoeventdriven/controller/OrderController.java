package com.example.demoeventdriven.controller;

import com.example.demoeventdriven.domain.Order;
import com.example.demoeventdriven.domain.dto.OrderDTO;
import com.example.demoeventdriven.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("order")
    public ResponseEntity placeOrder(@RequestBody @NotNull(message = "Invalid Order") OrderDTO orderDTO) {
        try {
            Order order = orderService.placeOrder(orderDTO);
            return ResponseEntity.ok(order);
        }
        catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getLocalizedMessage());
        }
    }

    @GetMapping("order/status/{id}")
    public ResponseEntity checkStatus(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(orderService.statusCheck(id));
        }
        catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getLocalizedMessage());
        }
    }
}
