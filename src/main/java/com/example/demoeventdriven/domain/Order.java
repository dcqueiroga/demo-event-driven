package com.example.demoeventdriven.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
public class Order {

    @NotBlank
    private String itemName;
    private UUID id;
    private OrderStatus status;
}
