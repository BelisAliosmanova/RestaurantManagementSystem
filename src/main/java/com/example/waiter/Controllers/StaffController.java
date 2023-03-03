package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.Staff;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Repositories.StaffRepository;
import com.example.waiter.Services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Controller
public class StaffController {
    @Autowired
    StaffService staffService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return staffService.showRegistrationForm(model);
    }

    @PostMapping("/process_register")
    public ModelAndView processRegister(@Valid Staff staff, BindingResult bindingResult) {
        return staffService.processRegister(staff, bindingResult);
    }

    @GetMapping("/homePageWaiter")
    public String homePageWaiter(Model model) {
        return staffService.homePageWaiter(model);
    }

    @GetMapping("/homePageCook")
    public String homePageCook(Model model) {
        return staffService.homePageCook(model);
    }

    @GetMapping("/waiterReference")
    public String waiterReference(@RequestParam(defaultValue = "asc") String sort, Model model,
                                  @RequestParam(name = "sort", defaultValue = "") String sortParam,
                                  @RequestParam(name = "startDate", defaultValue = "") String startDate,
                                  @RequestParam(name = "endDate", defaultValue = "") String endDate) throws ParseException {
        return staffService.waiterReference(sort, model, sortParam, startDate,endDate);
    }
    @GetMapping("/cookReference")
    public String cookReference(Model model){
        return staffService.cookReference(model);
    }
}