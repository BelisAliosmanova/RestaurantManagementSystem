package com.example.waiter.Repositories;

import com.example.waiter.Entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
