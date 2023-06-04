package com.example.waiter.Exceptions;

public class CantDeleteDishOrDrinkException extends RuntimeException {
    public CantDeleteDishOrDrinkException (String message){
        super(message);
    }
}
