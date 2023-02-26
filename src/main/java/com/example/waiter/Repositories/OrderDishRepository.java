package com.example.waiter.Repositories;

import com.example.waiter.Entities.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDishRepository extends JpaRepository<OrderDish, Long> {
}
