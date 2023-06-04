package com.example.waiter.Controllers;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class DishController {
    @Autowired
    DishService dishService;
    @PostMapping("/dishSubmit")
    public ModelAndView addDish(@Valid Dish dish, BindingResult bindingResult) {
        return dishService.addDishSubmit(dish, bindingResult);
    }

    @GetMapping("/addDish")
    public String addDish(Model model) {
        return dishService.addDish(model);
    }
    @GetMapping("/editDish/{dishId}")
    public String editDish(@PathVariable(name = "dishId") Long dishId, Model model) {
        return dishService.editDish(dishId, model);
    }

    @PostMapping("/updateDish")
    public ModelAndView updateDish(@Valid Dish dish, BindingResult bindingResult, Model model) {
        return dishService.updateDish(dish, bindingResult, model);
    }
    @PostMapping("/deleteDish/{dishId}")
    public ModelAndView deleteDish(@PathVariable(name = "dishId") Long dishId, Model model) {
        return dishService.deleteDish(dishId, model);
    }
}