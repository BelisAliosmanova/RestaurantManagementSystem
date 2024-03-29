package com.example.waiter.Services;

import com.example.waiter.Entities.Drink;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Exceptions.CantDeleteDishOrDrinkException;
import com.example.waiter.Repositories.DrinkRepository;
import com.example.waiter.Repositories.OrderDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Service
public class DrinkService {
    @Autowired
    DrinkRepository drinkRepository;
    @Autowired
    OrderDishRepository orderDishRepository;
    public ModelAndView addDrink(Drink drink, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/restaurantMenu/addDrink");
        } else {
            drinkRepository.save(drink);
            return new ModelAndView("redirect:/homePageWaiter");
        }
    }

    public String addDrink(Model model) {
        model.addAttribute("drink", new Drink());
        return ("/restaurantMenu/addDrink");
    }

    public String editDrink(@PathVariable(name = "drinkId") Long drinkId, Model model) {
        Optional<Drink> optionalDrink = drinkRepository.findById(drinkId);
        if (optionalDrink.isPresent()) {
            model.addAttribute("drink", optionalDrink.get());
        } else {
            model.addAttribute("drink", "Error!");
            model.addAttribute("errorMsg", " Not existing drink with id = " + drinkId);
        }
        return "/restaurantMenu/editDrink";
    }


    public ModelAndView updateDrink(Drink drink, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/restaurantMenu/editDrink");
        } else {
            drinkRepository.save(drink);
            return new ModelAndView("redirect:/restaurantMenu");
        }
    }

    public ModelAndView deleteDrink(@PathVariable(name = "drinkId") Long drinkId, Model model) {
        List<OrderDish> orderDishList = orderDishRepository.findAll();
        for (OrderDish orderDish : orderDishList) {
            if (orderDish.getDish() != null) {
                if (orderDish.getDrink().getId().equals(drinkId) && !(orderDish.getOrder().getStatus().equals(OrderStatus.PAID))) {
                    model.addAttribute("drink", "Error!");
                    model.addAttribute("errorMsg", " YOU CANNOT DELETE THAT DRINK IT IS ORDERED! YOU CAN DELETE IR WHEN THE ORDER IS PAID");
                } else if (orderDish.getDish().getId().equals(drinkId)) {
                    orderDishRepository.deleteById(orderDish.getId());
                }
            }
        }
        drinkRepository.deleteById(drinkId);
        return new ModelAndView("redirect:/restaurantMenu");
    }
}
