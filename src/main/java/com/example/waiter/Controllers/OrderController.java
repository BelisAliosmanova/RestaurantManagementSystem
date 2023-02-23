package com.example.waiter.Controllers;

import com.example.waiter.Entities.Drink;
import com.example.waiter.Entities.Order;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.example.waiter.Enums.OrderStatus.ACTIVE;

@Controller
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/orderSubmit")
    public ModelAndView orderSubmit(@Valid Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/addOrder");
        } else {
            order.setStatus(ACTIVE);
            orderRepository.save(order);
            return new ModelAndView("redirect:/addOrderDetails");
        }
    }

    @GetMapping("/addOrder")
    public String addOrder(Model model) {
        model.addAttribute("order", new Order());
        return ("/addOrder");
    }

}
