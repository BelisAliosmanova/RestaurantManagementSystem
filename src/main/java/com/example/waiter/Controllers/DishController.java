package com.example.waiter.Controllers;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class DishController {
    @Autowired
    DishRepository dishRepository;


    @PostMapping("/dishSubmit")
    public ModelAndView addDish(@Valid Dish dish, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/addDish");
        } else {
            dishRepository.save(dish);
            return new ModelAndView("redirect:/homePageWaiter");
        }
    }
    @GetMapping("/restaurantMenu")
    public String getAllDishes(Model model){
        Iterable<Dish> allDishes = dishRepository.findAll();
        model.addAttribute("allDishes", allDishes);
        return "/restaurantMenu";
    }

    @GetMapping("/addDish")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        return ("/addDish");
    }
    @GetMapping("/editDish/{dishId}")
    public String editDish(@PathVariable(name = "dishId") Long dishId, Model model) {
        Optional<Dish> optionalDish = dishRepository.findById(dishId);
        if (optionalDish.isPresent()) {
            model.addAttribute("dish", optionalDish.get());
        } else {
            model.addAttribute("dish", "Error!");
            model.addAttribute("errorMsg", " Not existing dish with id = " + dishId);
        }
        return "/editDish";
    }

    @PostMapping("/updateDish")
    public ModelAndView updateDish(@Valid Dish dish, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            ;
            return new ModelAndView("/editDish");
        } else {
            dishRepository.save(dish);
            return new ModelAndView("redirect:/restaurantMenu");
        }
    }
    @PostMapping("/deleteDish/{dishId}")
    public ModelAndView deleteDish(@PathVariable(name = "dishId") Long dishId) {
        dishRepository.deleteById(dishId);
        return new ModelAndView("redirect:/restaurantMenu");
    }
}
