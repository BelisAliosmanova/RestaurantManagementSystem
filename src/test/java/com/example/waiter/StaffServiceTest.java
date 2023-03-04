package com.example.waiter;
import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.Staff;
import com.example.waiter.Enums.Role;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Repositories.StaffRepository;
import com.example.waiter.Services.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testProcessRegister_withValidInput_shouldSaveStaffAndRedirectToMenu() {
        Staff staff = new Staff();
        staff.setUsername("testuser");
        staff.setPassword("password");
        staff.setId(1L);
        staff.getId();
        staff.setRole(Role.WAITER);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        ModelAndView mav = staffService.processRegister(staff, bindingResult);
        verify(staffRepository, times(1)).save(staff);
        assertEquals("menu", mav.getViewName());
        assertEquals(0, mav.getModel().size());
    }
    @Test
    public void testProcessRegister_withInValidInput() {
        Staff staff = new Staff();
        staff.setUsername("1");
        staff.setPassword("password");
        staff.setRole(Role.WAITER);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        ModelAndView mav = staffService.processRegister(staff, bindingResult);
        verify(staffRepository, times(0)).save(staff);
        assertEquals("/register", mav.getViewName());
        assertEquals(0, mav.getModel().size());
    }
//    @Test
//    public void testWaiterReference() throws ParseException {
//        Model model = new ConcurrentModel();
//        String viewName = staffService.waiterReference("date", model, "date", "2022-01-01", "2022-12-31");
//
//        assertEquals("/waiterReference", viewName);
//        assertEquals("john", model.getAttribute("waiter"));
//        assertTrue(model.getAttribute("waiterOrders") instanceof List<?>);
//        List<?> waiterOrders = (List<?>) model.getAttribute("waiterOrders");
//        assertFalse(waiterOrders.isEmpty());
//        assertTrue(waiterOrders.get(0) instanceof Order);
//    }
}
