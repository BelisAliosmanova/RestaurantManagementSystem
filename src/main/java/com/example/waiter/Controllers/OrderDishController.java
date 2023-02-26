package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private DrinkRepository drinkRepository;

    @GetMapping("/addOrderDish")
    public String addOrderDish(Model model) {
        model.addAttribute("orderDish", new OrderDish());
        model.addAttribute("dishes", dishRepository.findAll());
        model.addAttribute("drinks", drinkRepository.findAll());
        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("orderId", orderRepository.findFirstByOrderByIdDesc());
        model.addAttribute("order", new Order());
        return ("/addOrderDish");
    }

    @PostMapping("/orderSubmitDish")
    public ModelAndView addOrderDish(@Valid OrderDish orderDish, BindingResult bindingResult, Model model,
                                     @RequestParam(value = "addAnotherDish", required = false) boolean addAnotherDish) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dishes", dishRepository.findAll());
            model.addAttribute("drinks", drinkRepository.findAll());
            model.addAttribute("orders", orderRepository.findAll());
            return new ModelAndView("/addOrderDish");
        } else if (!addAnotherDish) {
            orderDish.setOrder(orderRepository.findFirstByOrderByIdDesc());
            orderDishRepository.save(orderDish);
            return new ModelAndView("redirect:/homePageWaiter");
        } else {
            model.addAttribute("orderId", orderRepository.findFirstByOrderByIdDesc());
            orderDish.setOrder(orderRepository.findFirstByOrderByIdDesc());
            orderDishRepository.save(orderDish);
            return new ModelAndView("redirect:/addOrderDish");
        }
    }
}
