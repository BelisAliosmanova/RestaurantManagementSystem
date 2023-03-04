package com.example.waiter.Controllers;

import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Exceptions.NoOrderDishException;
import com.example.waiter.Services.OrderDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class OrderDishController {
    @Autowired
    private OrderDishService orderDishService;

    @GetMapping("/addOrderDish")
    public String addOrderDish(Model model) {
        return orderDishService.addOrderDish(model);
    }

    @PostMapping("/orderSubmitDish")
    public ModelAndView addOrderDish(@Valid OrderDish orderDish, BindingResult bindingResult, Model model,
                                     @RequestParam(value = "addAnotherDish", required = false) boolean addAnotherDish) {
        return orderDishService.addOrderDish(orderDish, bindingResult, model, addAnotherDish);
    }
    @ExceptionHandler(NoOrderDishException.class)
    @GetMapping("/error")
    public String handleNoOrderDishException(NoOrderDishException ex, Model model) {
        return orderDishService.handleNoOrderDishException(ex, model);
    }
    @GetMapping("/editOrderDetails")
    public String editOrderDetails(Model model){
        return orderDishService.editOrderDetails(model);
    }
    @PostMapping ("/delete/{orderDishId}")
    public ModelAndView deleteOrderDish(@PathVariable (name="orderDishId") Long orderDishId, Model model ) {
        return orderDishService.deleteOrderDish(orderDishId, model);
    }
    public static Long orderId;
    @GetMapping ("/editOrderDish/{orderDishId}")
    public String editOrderDish(@PathVariable (name="orderDishId") Long orderDishId, Model model ) {
        return orderDishService.editOrderDish(orderDishId, model);
    }
    @PostMapping("/updateOrderDish")
    public ModelAndView updateOrderDish(@Valid OrderDish orderDish, BindingResult bindingResult) {
        return orderDishService.updateOrderDish(orderDish, bindingResult);
    }
}
