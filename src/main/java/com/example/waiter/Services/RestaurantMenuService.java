package com.example.waiter.Services;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Drink;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class RestaurantMenuService {
    @Autowired
    DishRepository dishRepository;
    @Autowired
    DrinkRepository drinkRepository;

    public String getAllDishes(Model model){
        Iterable<Dish> allDishes = dishRepository.findAll();
        model.addAttribute("allDishes", allDishes);
        Iterable<Drink> allDrinks = drinkRepository.findAll();
        model.addAttribute("allDrinks", allDrinks);
        return "/restaurantMenu/restaurantMenu";
    }
}
