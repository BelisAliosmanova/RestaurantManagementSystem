package com.example.waiter.Repositories;

import com.example.waiter.Entities.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    OrderDetails findTopByOrderByIdDesc();
}
