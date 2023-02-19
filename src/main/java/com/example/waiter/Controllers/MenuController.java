package com.example.waiter.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
    @GetMapping("/menu")
    public String menu() {
        return "/menu";
    }
//    @GetMapping("/homePageWaiter")
//    public String homePageWaiter() {
//        return "/homePageWaiter";
//    }
    @GetMapping("/homePageCook")
    public String homePageCook() {
        return "/homePageCook";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

}
