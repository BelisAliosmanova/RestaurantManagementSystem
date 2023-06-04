package com.example.waiter.Controllers;

import com.example.waiter.Entities.Drink;
import com.example.waiter.Services.DrinkService;
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
public class DrinkController {
    @Autowired
    DrinkService drinkService;
    @PostMapping("/drinkSubmit")
    public ModelAndView addDrink(@Valid Drink drink, BindingResult bindingResult) {
        return drinkService.addDrink(drink, bindingResult);
    }

    @GetMapping("/addDrink")
    public String addDrink(Model model) {
        return drinkService.addDrink(model);
    }
    @GetMapping("/editDrink/{drinkId}")
    public String editDrink(@PathVariable(name = "drinkId") Long drinkId, Model model) {
        return drinkService.editDrink(drinkId, model);
    }

    @PostMapping("/updateDrink")
    public ModelAndView updateDrink(@Valid Drink drink, BindingResult bindingResult, Model model) {
        return drinkService.updateDrink(drink, bindingResult, model);
    }
    @PostMapping("/deleteDrink/{drinkId}")
    public ModelAndView deleteDrink(@PathVariable(name = "drinkId") Long drinkId, Model model) {
        return drinkService.deleteDrink(drinkId, model);
    }
}
