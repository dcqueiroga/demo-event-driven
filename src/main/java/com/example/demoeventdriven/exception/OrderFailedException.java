package com.example.demoeventdriven.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class OrderFailedException extends RuntimeException {

    public OrderFailedException(String message) {
        super(message);
    }
}
