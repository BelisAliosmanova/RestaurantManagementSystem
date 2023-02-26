package com.example.waiter.Repositories;

import com.example.waiter.Entities.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order findFirstByOrderByIdDesc();
}
