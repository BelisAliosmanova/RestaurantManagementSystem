package com.example.waiter.Controllers;

import com.example.waiter.Security.MyStaffDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class WaiterController {
    MyStaffDetails myStaffDetails;

    @GetMapping("/homePageWaiter")
    public String getName(Model model) {
        System.out.println(myStaffDetails.getUsername());
        model.addAttribute("username", myStaffDetails.getUsername());
        return "homePageWaiter";
    }
}
