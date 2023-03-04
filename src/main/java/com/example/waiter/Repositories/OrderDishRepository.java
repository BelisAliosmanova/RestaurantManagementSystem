package com.example.waiter.Repositories;

import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface OrderDishRepository extends JpaRepository<OrderDish, Long> {


    List<OrderDish> findByOrderId(Long orderId);
    @Query("SELECT od FROM OrderDish od WHERE od.order.orderDate = :orderDate")
    List<OrderDish> findByOrderDate(@Param("orderDate") Date orderDate);
}
