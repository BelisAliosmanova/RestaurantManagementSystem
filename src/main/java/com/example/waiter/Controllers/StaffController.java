package com.example.waiter.Controllers;

import com.example.waiter.Entities.Staff;
import com.example.waiter.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    StaffRepository staffRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "/register";
    }
    @PostMapping("/process_register")
    public ModelAndView processRegister(@Valid Staff staff, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/register");
        } else{
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(staff.getPassword());
            staff.setEnabled(true);
            staff.setPassword(encodedPassword);
            staffRepository.save(staff);
            return new ModelAndView("redirect:/menu");
        }
    }
}
