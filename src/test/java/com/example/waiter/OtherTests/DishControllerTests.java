package com.example.waiter.OtherTests;

import com.example.waiter.Controllers.DishController;
import com.example.waiter.Services.DishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishControllerTests {
    @Mock
    private DishService dishService;

    @Mock
    private Model model;

    @InjectMocks
    private DishController dishController;

    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addDish() {
        String expectedViewName = "/addDish";
        when(dishService.addDish(model)).thenReturn(expectedViewName);
        String actualViewName = dishController.addDish(model);
        verify(dishService, times(1)).addDish(model);
        assertEquals(expectedViewName, actualViewName);
    }
}
