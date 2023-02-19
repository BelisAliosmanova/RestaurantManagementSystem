package com.example.waiter.Services;

import com.example.waiter.Entities.Staff;
import com.example.waiter.Enums.Role;
import com.example.waiter.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Service
public class StaffService {
    @Autowired
    StaffRepository staffRepository;

    public boolean processRegister(@Valid Staff staff, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return false;
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(staff.getPassword());
            staff.setEnabled(true);
            staff.setPassword(encodedPassword);
            if ((staff.getRole().equals(Role.COOK)) || (staff.getRole().equals(Role.WAITER))) {
                System.out.println(staff.getRole());
                staffRepository.save(staff);
                return true;
            } else {
                return false;
            }
        }
    }
}
