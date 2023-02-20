package com.example.waiter.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
    @GetMapping("/menu")
    public String menu() {
        return "/menu";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

}
