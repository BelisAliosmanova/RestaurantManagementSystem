package com.example.waiter.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
    @GetMapping("/menu")
    public String menu() {
        return "/home/menu";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

}
