package com.example.waiter.Services;

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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private OrderDishRepository orderDishRepository;

    public String addOrder(Model model) {
        model.addAttribute("order", new Order());
        return ("/addOrder");
    }

    public ModelAndView addOrder(Order order, BindingResult bindingResult) {
        System.out.println(order.getOrderDate());
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/addOrder");
        } else {
            Iterable<Order> allOrders = orderRepository.findAll();
            for (Order newOrder : allOrders) {
                if ((order.getTableNum() == newOrder.getTableNum()) && (newOrder.getStatus().equals(OrderStatus.ACTIVE))) {
                    throw new NotFreeTableException("THIS TABLE IS NOT FREE NOW!");

                }
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Staff staff = staffRepository.getStaffByUsername(auth.getName());
            order.setStaff(staff);
            order.setStatus(OrderStatus.valueOf("ACTIVE"));
            orderRepository.save(order);
            return new ModelAndView("redirect:/addOrderDish");
        }
    }

    public String activeOrders(Model model) {
        Iterable<Order> allOrders = orderRepository.findAll();
        List<Order> activeOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getStatus().equals(OrderStatus.ACTIVE) || order.getStatus().equals(OrderStatus.SERVED)) {
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

    public String editOrder(Long orderId, Model model, Principal principal) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            model.addAttribute("order", optionalOrder.get());
        } else {
            String username = principal.getName();
            Staff staff = staffRepository.findByUsername(username);
            model.addAttribute("staffRole", staff.getRole());
            model.addAttribute("order", "Error!");
            model.addAttribute("errorMsg", "Not existing order with id: " + orderId);
        }
        return "/editOrder";
    }

    public ModelAndView updateOrder(Order order, BindingResult bindingResult, Model model) {
        List<OrderDish> orderDishes = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/editOrder");
        } else {
            if (order.getStatus().equals(OrderStatus.PAID)) {
                for (OrderDish orderDish : orderDishRepository.findAll()) {
                    if (orderDish.getOrder().getId().equals(order.getId())) {
                        orderDishes.add(orderDish);
                    }
                }
                model.addAttribute("order", order);
                model.addAttribute("orderDishes", orderDishes);
                orderRepository.save(order);
                return new ModelAndView("/orderSummary");
            }
            orderRepository.save(order);
            return new ModelAndView("redirect:/activeOrders");
        }
    }

    public String editOrderStatusCook(Long orderId, Model model) {
        System.out.println(" order id " + orderId);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            model.addAttribute("order", optionalOrder.get());
        } else {
            model.addAttribute("order", "Error!");
            model.addAttribute("errorMsg", "Not existing order with id: " + orderId);
        }
        return "/editOrderStatusCook";
    }

    public ModelAndView updateOrderStatusCook(Order order, BindingResult bindingResult, Model model) {
        List<OrderDish> orderDishes = new ArrayList<>();
        System.out.println("id " + order.getId());
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/editOrderStatusCook");
        } else {
            orderRepository.save(order);
            return new ModelAndView("redirect:/activeOrdersCook");
        }
    }

    public String activeOrdersCook(Model model) {
        Iterable<Order> allOrders = orderRepository.findAll();
        List<Order> activeOrdersCook = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getStatus().equals(OrderStatus.ACTIVE) || order.getStatus().equals(OrderStatus.PREPARING)) {
                activeOrdersCook.add(order);
            }
        }
        model.addAttribute("activeOrdersCook", activeOrdersCook);
        return "/activeOrdersCook";
    }

    public String showOrderDetails(Long orderId, Model model) {
        List<OrderDish> activeOrders= orderDishRepository.findByOrderId(orderId);
                model.addAttribute("activeOrders",activeOrders);
        return   "/orderDetailsCook";
    }

}
