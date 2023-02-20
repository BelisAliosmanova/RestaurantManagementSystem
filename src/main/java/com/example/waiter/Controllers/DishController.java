package com.example.waiter.Controllers;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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

    @GetMapping("/addDish")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        return ("/addDish");
    }

}
