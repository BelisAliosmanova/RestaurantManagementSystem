package com.example.waiter.Repositories;

import com.example.waiter.Entities.OrderDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OderDetailsRepository extends CrudRepository<OrderDetails, Long> {
}
