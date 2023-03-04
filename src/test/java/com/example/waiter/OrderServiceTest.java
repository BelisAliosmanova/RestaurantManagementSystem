package com.example.waiter;

import com.example.waiter.Entities.Order;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Repositories.StaffRepository;
import com.example.waiter.Services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderService orderService;
    @Test
    void testAddOrder() {
        Model model = new ExtendedModelMap();
        String viewName = orderService.addOrder(model);
        assertEquals("/addOrder", viewName);
    }
    @Test
    void updateOrderStatusCook_shouldReturnRedirectAndSaveOrder() {
        Order order = new Order();
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = mock(Model.class);
        ModelAndView result = orderService.updateOrderStatusCook(order, bindingResult, model);
        verify(orderRepository).save(order);
        assertEquals("redirect:/activeOrdersCook", result.getViewName());
    }
    @Test
    void updateOrderStatusCook_shouldReturnEditOrderStatusCookViewWhenBindingResultHasErrors() {
        Order order = new Order();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = mock(Model.class);
        ModelAndView result = orderService.updateOrderStatusCook(order, bindingResult, model);
        assertEquals("/editOrderStatusCook", result.getViewName());
    }
}
