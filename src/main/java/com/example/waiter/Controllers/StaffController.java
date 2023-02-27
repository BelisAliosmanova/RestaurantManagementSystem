package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Entities.Staff;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Repositories.StaffRepository;
import com.example.waiter.Services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
public class StaffController {
    @Autowired
    StaffService staffService;
    @Autowired
    StaffRepository staffRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "/register";
    }

    @PostMapping("/process_register")
    public ModelAndView processRegister(@Valid Staff staff, BindingResult bindingResult) {
        return staffService.processRegister(staff, bindingResult);
    }

    @GetMapping("/homePageWaiter")
    public String homePageWaiter(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        return "/homePageWaiter";
    }

    @GetMapping("/homePageCook")
    public String homePageCook(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        return "/homePageCook";
    }

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/waiterReference")
    public String waiterReference(@RequestParam(defaultValue = "asc") String sort, Model model,
                                  @RequestParam(name = "sort", defaultValue = "") String sortParam,
                                  @RequestParam(name = "startDate", defaultValue = "") String startDate,
                                  @RequestParam(name = "endDate", defaultValue = "") String endDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Iterable<Order> orders = orderRepository.findAll();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Staff staff = staffRepository.findByUsername(auth.getName());
        List<Order> waiterOrders = new ArrayList<>();
        for (Order order : orders) {
            if(order.getStaff().equals(staff)){
                if(!(startDate.equals("") && endDate.equals(""))) {
                    if (order.getOrderDate().after(formatter.parse(startDate)) && (order.getOrderDate().before(formatter.parse(endDate)))) {
                        waiterOrders.add(order);
                    }
                } else{
                    waiterOrders.add(order);
                }
            }
        }
        if (sortParam.equals("date")) {
            waiterOrders.sort(Comparator.comparing(Order::getOrderDate));
        }
        System.out.println(startDate);
        System.out.println(endDate);
        model.addAttribute("waiter", auth.getName());
        model.addAttribute("waiterOrders", waiterOrders);
        return "/waiterReference";
    }
}