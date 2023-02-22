package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDetails;
import com.example.waiter.Repositories.OderDetailsRepository;
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
public class OrderDetailsController {
    @Autowired
    OderDetailsRepository oderDetailsRepository;

    @PostMapping("/orderDetailsSubmit")
    public ModelAndView orderDetailsSubmit(@Valid OrderDetails orderDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/addOrderDetails");
        } else {
            oderDetailsRepository.save(orderDetails);
            return new ModelAndView("redirect:/homePageWaiter");
        }
    }

    @GetMapping("/addOrderDetails")
    public String addOrderDetails(Model model) {
        model.addAttribute("orderDetails", new OrderDetails());
        return ("/addOrderDetails");
    }
}
