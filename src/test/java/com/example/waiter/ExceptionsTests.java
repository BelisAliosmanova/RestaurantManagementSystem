package com.example.waiter;

import com.example.waiter.Exceptions.IllegalDishOrDrinkCountException;
import com.example.waiter.Exceptions.NoOrderDishException;
import com.example.waiter.Exceptions.NotFreeTableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionsTests {

    @Test
    public void NotFreeTableExceptionTest() {
        String message = "Table is not free";
        NotFreeTableException exception = new NotFreeTableException(message);
        assertEquals(message, exception.getMessage());
    }
    @Test
    public void IllegalDishOrDrinkCountExceptionTest() {
        String message = "Illegal Dish Or Drink Count Exception";
        IllegalDishOrDrinkCountException exception = new IllegalDishOrDrinkCountException(message);
        assertEquals(message, exception.getMessage());
    }
    @Test
    public void NoOrderDishExceptionTest() {
        String message = "No Order Dish Exception";
        NoOrderDishException exception = new NoOrderDishException(message);
        assertEquals(message, exception.getMessage());
    }
}
