package com.example.waiter.Controllers;

import com.example.waiter.Entities.Staff;
import com.example.waiter.Enums.Role;
import com.example.waiter.Repositories.StaffRepository;
import com.example.waiter.Services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class StaffController {
    @Autowired
    StaffService staffService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "/register";
    }

    @PostMapping("/process_register")
    public ModelAndView processRegister(@Valid Staff staff, BindingResult bindingResult) {
        boolean result = staffService.processRegister(staff, bindingResult);
        if(!result){
            return new ModelAndView("/register");
        } else {
            return new ModelAndView("redirect:/menu");
        }
    }
}
