package com.example.waiter.Services;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class DishService {
    @Autowired
    DishRepository dishRepository;

    public ModelAndView addDishSubmit(Dish dish, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/restaurantMenu/addDish");
        } else {
            dishRepository.save(dish);
            return new ModelAndView("redirect:/homePageWaiter");
        }
    }

    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        return "/restaurantMenu/addDish";
    }

    public String editDish(@PathVariable(name = "dishId") Long dishId, Model model) {
        Optional<Dish> optionalDish = dishRepository.findById(dishId);
        if (optionalDish.isPresent()) {
            model.addAttribute("dish", optionalDish.get());
        } else {
            model.addAttribute("dish", "Error!");
            model.addAttribute("errorMsg", " Not existing dish with id = " + dishId);
        }
        return "/restaurantMenu/editDish";
    }

    public ModelAndView updateDish(@Valid Dish dish, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/restaurantMenu/editDish");
        } else {
            dishRepository.save(dish);
            return new ModelAndView("redirect:/restaurantMenu");
        }
    }

    public ModelAndView deleteDish(@PathVariable(name = "dishId") Long dishId) {
        dishRepository.deleteById(dishId);
        return new ModelAndView("redirect:/restaurantMenu");
    }
}
