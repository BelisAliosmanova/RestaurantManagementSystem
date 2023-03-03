package com.example.waiter.Controllers;

import com.example.waiter.Services.RestaurantMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class RestaurantMenu {
    @Autowired
    private RestaurantMenuService restaurantMenuService;
    @GetMapping("/restaurantMenu")
    public String getAllDishes(Model model){
        return restaurantMenuService.getAllDishes(model);
    }
}
