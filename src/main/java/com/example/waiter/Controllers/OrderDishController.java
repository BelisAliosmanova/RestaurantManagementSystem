package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class OrderDishController {
    @Autowired
    private OrderDishRepository orderDishRepository;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/addOrderDish")
    public String addOrderDish(Model model) {
        model.addAttribute("orderDish", new OrderDish());
        model.addAttribute("dishes", dishRepository.findAll());
        model.addAttribute("orders", orderRepository.findAll());
        return ("/addOrderDish");
    }

    @PostMapping("/orderSubmitDish")
    public ModelAndView addOrderDish(@Valid OrderDish orderDish, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println(orderDish);
            model.addAttribute("dishes", dishRepository.findAll());
            model.addAttribute("orders", orderRepository.findAll());
            return new ModelAndView("/addOrderDish");
        } else {
            orderDishRepository.save(orderDish);
            return new ModelAndView("redirect:/homePageWaiter");
        }
    }
}
