package com.example.waiter.OtherTests;

import com.example.waiter.Entities.Order;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.scheduler.OrderScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderSchedulerTests {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderScheduler orderScheduler;

    @Test
    public void testDeleteNullOrders() {
        List<Order> orders = Arrays.asList(new Order(), new Order(), new Order());
        orders.get(0).setTotalPrice(0.5);
        orders.get(0).setId(1L);
        orders.get(1).setTotalPrice(1.5);
        orders.get(1).setId(2L);
        orders.get(2).setTotalPrice(0.0);
        orders.get(1).setId(2L);
        when(orderRepository.findAll()).thenReturn(orders);
        orderScheduler.deleteNullOrders();
        Mockito.verify(orderRepository, times(1)).deleteById(orders.get(0).getId());
        Mockito.verify(orderRepository, never()).deleteById(orders.get(1).getId());
        Mockito.verify(orderRepository, times(1)).deleteById(orders.get(2).getId());
    }
}
