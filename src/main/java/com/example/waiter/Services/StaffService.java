package com.example.waiter.Services;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.Staff;
import com.example.waiter.Enums.Role;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class StaffService {
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    OrderRepository orderRepository;

    public ModelAndView processRegister(Staff staff, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/register");
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(staff.getPassword());
            staff.setEnabled(true);
            staff.setPassword(encodedPassword);
            if ((staff.getRole().equals(Role.COOK)) || (staff.getRole().equals(Role.WAITER))) {
                System.out.println(staff.getRole());
                staffRepository.save(staff);
                return new ModelAndView("/menu");
            } else {
                return new ModelAndView("/register");
            }
        }
    }
    public String showRegistrationForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "/register";
    }
    public String homePageWaiter(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        return "/home/homePageWaiter";
    }
    public String homePageCook(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        return "/home/homePageCook";
    }
    public String waiterReference(String sort, Model model, String sortParam,String startDate, String endDate) throws ParseException {
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
        return "/reference/waiterReference";
    }
    private List<Object[]> getOrdersGroupedByOrderDate() {
        return orderRepository.groupByOrderDate();
    }
    public String cookReference(Model model){
        model.addAttribute("ordersGroupedByOrderDate", getOrdersGroupedByOrderDate());
        return "/reference/cookReference";
    }
}
