package com.example.waiter.Repositories;

import com.example.waiter.Entities.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order findFirstByOrderByIdDesc();
    @Query("SELECT o.orderDate, COUNT(o) FROM Order o GROUP BY o.orderDate ORDER BY o.orderDate DESC")
    List<Object[]> groupByOrderDate();
}
