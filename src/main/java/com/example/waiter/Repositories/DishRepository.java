package com.example.waiter.Repositories;

import com.example.waiter.Entities.Dish;
import org.hibernate.mapping.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Long> {
    @Query (value="SELECT * FROM Dish WHERE dish.type LIKE %:SALAD%", nativeQuery=true)
    List<Dish>  getSalads();


}
