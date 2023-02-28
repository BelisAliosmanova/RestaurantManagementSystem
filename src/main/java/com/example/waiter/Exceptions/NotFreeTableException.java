package com.example.waiter.Exceptions;

public class NotFreeTableException extends RuntimeException{
    public NotFreeTableException(String message) {
        super(message);
    }
}
