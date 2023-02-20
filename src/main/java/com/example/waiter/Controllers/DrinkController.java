package com.example.waiter.Controllers;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Drink;
import com.example.waiter.Repositories.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class DrinkController {
    @Autowired
    DrinkRepository drinkRepository;
    @PostMapping("/drinkSubmit")
    public ModelAndView addDrink(@Valid Drink drink, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/addDrink");
        } else {
            drinkRepository.save(drink);
            return new ModelAndView("redirect:/homePageWaiter");
        }
    }

    @GetMapping("/addDrink")
    public String addDrink(Model model) {
        model.addAttribute("drink", new Drink());
        return ("/addDrink");
    }
}
