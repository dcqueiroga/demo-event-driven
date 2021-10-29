package com.example.demoeventdriven.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderDTO {

    @NotBlank
    private String itemName;
}
