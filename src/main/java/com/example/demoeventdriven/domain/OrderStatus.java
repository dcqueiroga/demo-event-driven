package com.example.demoeventdriven.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {

    PENDING("Pending"),
    INVENTORY_CHECKING("Inventory Checking"),
    OUT_OF_STOCK("Out of stock"),
    SHIPPED("Shipped"),
    CANCELED("Canceled");

    private final String name;
}
