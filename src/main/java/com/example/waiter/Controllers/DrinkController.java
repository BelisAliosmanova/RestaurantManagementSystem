package com.example.waiter.Controllers;

import com.example.waiter.Entities.Drink;
import com.example.waiter.Repositories.DrinkRepository;
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
    @GetMapping("/editDrink/{drinkId}")
    public String editDrink(@PathVariable(name = "drinkId") Long drinkId, Model model) {
        Optional<Drink> optionalDrink = drinkRepository.findById(drinkId);
        if (optionalDrink.isPresent()) {
            model.addAttribute("drink", optionalDrink.get());
        } else {
            model.addAttribute("drink", "Error!");
            model.addAttribute("errorMsg", " Not existing drink with id = " + drinkId);
        }
        return "/editDrink";
    }

    @PostMapping("/updateDrink")
    public ModelAndView updateDrink(@Valid Drink drink, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/editDrink");
        } else {
            drinkRepository.save(drink);
            return new ModelAndView("redirect:/restaurantMenu");
        }
    }
    @PostMapping("/deleteDrink/{drinkId}")
    public ModelAndView deleteDrink(@PathVariable(name = "drinkId") Long drinkId) {
        drinkRepository.deleteById(drinkId);
        return new ModelAndView("redirect:/restaurantMenu");
    }
}
