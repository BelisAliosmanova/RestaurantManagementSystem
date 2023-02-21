package com.example.waiter.Controllers;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Drink;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class RestaurantMenu {
    @Autowired
    DishRepository dishRepository;
    @Autowired
    DrinkRepository drinkRepository;
    @GetMapping("/restaurantMenu")
    public String getAllDishes(Model model){
        Iterable<Dish> allDishes = dishRepository.findAll();
        model.addAttribute("allDishes", allDishes);
        Iterable<Drink> allDrinks = drinkRepository.findAll();
        model.addAttribute("allDrinks", allDrinks);
        return "/restaurantMenu";
    }
}
