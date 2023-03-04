package com.example.waiter.Repositories;

import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDishRepository extends JpaRepository<OrderDish, Long> {
//    @Query("SELECT u FROM Staff u WHERE u.username = :username")
//    public Staff getStaffByUsername(@Param("username") String username);
}
