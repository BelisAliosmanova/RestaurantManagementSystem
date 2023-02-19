package com.example.waiter.Repositories;

import com.example.waiter.Entities.Dish;
import org.springframework.data.repository.CrudRepository;

public interface DishRepository extends CrudRepository<Dish, Long> {
}
