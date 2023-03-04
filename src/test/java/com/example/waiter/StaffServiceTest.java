package com.example.waiter;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.Staff;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Repositories.StaffRepository;
import com.example.waiter.Services.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {
    @InjectMocks
    StaffService staffService;
    @Mock
    StaffRepository staffRepository;
    @Mock
    OrderRepository orderRepository;

    @Test
    void register() {
        Model model = new ExtendedModelMap();
        String viewName = staffService.showRegistrationForm(model);
        assertEquals("/register", viewName);
    }

    @Test
    void cookReference() {
        Model model = new ExtendedModelMap();
        String viewName = staffService.cookReference(model);
        assertEquals("/cookReference", viewName);
    }

    @Test
    public void homePageWaiterShouldAddUsernameToModel() throws Exception {
        Authentication authentication = new TestingAuthenticationToken("user", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Model model = mock(Model.class);
        String expectedUsername = "user";
        String viewName = new StaffService().homePageWaiter(model);
        assertEquals("/homePageWaiter", viewName);
    }

    @Test
    public void homePageCookShouldAddUsernameToModel() throws Exception {
        Authentication authentication = new TestingAuthenticationToken("user", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Model model = mock(Model.class);
        String expectedUsername = "user";
        String viewName = new StaffService().homePageCook(model);
        assertEquals("/homePageCook", viewName);
    }


}

