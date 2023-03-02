package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Entities.Staff;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Exceptions.NotFreeTableException;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDishRepository orderDishRepository;
    @Autowired
    StaffRepository staffRepository;
    @GetMapping("/addOrder")
    public String addOrder(Model model) {
        model.addAttribute("order", new Order());
        return ("/addOrder");
    }

    @PostMapping("/orderSubmit")
    public ModelAndView addOrder(@Valid Order order, BindingResult bindingResult) {
        System.out.println(order.getOrderDate());
        if(bindingResult.hasErrors()){
            return new ModelAndView("/addOrder");
        } else {
            Iterable<Order> allOrders = orderRepository.findAll();
            for (Order newOrder: allOrders) {
                if((order.getTableNum()== newOrder.getTableNum()) && (newOrder.getStatus().equals(OrderStatus.ACTIVE))){
                    throw new NotFreeTableException("THIS TABLE IS NOT FREE NOW!");

                }
            }
            Authentication auth= SecurityContextHolder.getContext().getAuthentication();
            Staff staff = staffRepository.getStaffByUsername(auth.getName());
            order.setStaff(staff);
            order.setStatus(OrderStatus.valueOf("ACTIVE"));
            orderRepository.save(order);
            return new ModelAndView("redirect:/addOrderDish");
        }
    }
    @GetMapping("/activeOrders")
    public String activeOrders(Model model) {
        Iterable<Order> allOrders = orderRepository.findAll();
        List<Order> activeOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getStatus().equals(OrderStatus.ACTIVE) || order.getStatus().equals(OrderStatus.SERVED) ) {
                activeOrders.add(order);
            }
        }
        model.addAttribute("activeOrders", activeOrders);
        return "/activeOrders";
    }
    @ExceptionHandler(NotFreeTableException.class)
    @GetMapping("/error1")
    public String NotFreeTableException(NotFreeTableException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

}
