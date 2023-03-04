package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Exceptions.NotFreeTableException;
import com.example.waiter.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/addOrder")
    public String addOrder(Model model) {
        return orderService.addOrder(model);
    }

    @PostMapping("/orderSubmit")
    public ModelAndView addOrder(@Valid Order order, BindingResult bindingResult) {
        return orderService.addOrder(order, bindingResult);
    }

    @GetMapping("/activeOrders")
    public String activeOrders(Model model) {
        return orderService.activeOrders(model);
    }

    @ExceptionHandler(NotFreeTableException.class)
    @GetMapping("/error1")
    public String NotFreeTableException(NotFreeTableException ex, Model model) {
        return orderService.NotFreeTableException(ex, model);
    }

    @GetMapping("/editOrder/{orderId}")
    public String editOrder(@PathVariable(name = "orderId") Long orderId, Model model, Principal principal) {
        return orderService.editOrder(orderId, model, principal);
    }

    @PostMapping("/updateOrder")
    public ModelAndView updateOrder(@Valid Order order, BindingResult bindingResult, Model model) {
        return orderService.updateOrder(order, bindingResult, model);
    }

    @GetMapping("/activeOrdersCook")
    public String activeOrdersCook(Model model) {
        return orderService.activeOrdersCook(model);
    }

    @GetMapping("/editOrderStatusCook/{orderId}")
    public String editOrderStatus(@PathVariable(name = "orderId") Long orderId, Model model) {
        return orderService.editOrderStatusCook(orderId, model);
    }

    @PostMapping("/updateOrderStatusCook")
    public ModelAndView updateOrderStatusCook(@Valid Order order, BindingResult bindingResult, Model model) {
        return orderService.updateOrderStatusCook(order, bindingResult, model);
    }
    @GetMapping("/orderDetailsCook/{orderId}")
    public String showOrderDetails(@PathVariable(name = "orderId") Long orderId, Model model) {
        return orderService.showOrderDetails(orderId, model);
    }
    @GetMapping("/orderDetailsByDate/{orderDate}")
    public String showOrderDetailsByDate(@PathVariable(name = "orderDate") Date orderDate, Model model) throws ParseException {
        return orderService.showOrderDetailsByDate(orderDate, model);
    }

}

