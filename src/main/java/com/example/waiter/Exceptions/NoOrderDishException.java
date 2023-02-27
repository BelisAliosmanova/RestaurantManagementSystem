package com.example.waiter.Exceptions;

public class NoOrderDishException extends RuntimeException {
    public NoOrderDishException(String message) {
        super(message);
    }
}
