package com.example.waiter.Exceptions;

public class IllegalDishOrDrinkCountException extends RuntimeException{
    public IllegalDishOrDrinkCountException(String message) {
        super(message);
    }
}
