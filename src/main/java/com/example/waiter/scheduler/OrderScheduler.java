package com.example.waiter.scheduler;

import com.example.waiter.Entities.Order;
import com.example.waiter.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderScheduler {
    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(fixedRate = 1800000)
    public void deleteNullOrders() {
        Iterable<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getTotalPrice() < 1) {
                orderRepository.deleteById(order.getId());
            }
        }
    }
}
