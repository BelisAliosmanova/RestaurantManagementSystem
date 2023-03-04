package com.example.waiter;

import com.example.waiter.Controllers.OrderController;
import com.example.waiter.Services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTests {
    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testAddOrder() {
        String expected = "addOrder";
        when(orderService.addOrder(model)).thenReturn(expected);
        String actual = orderController.addOrder(model);
        Mockito.verify(orderService, times(1)).addOrder(model);
        assertEquals(expected, actual);
    }
}
