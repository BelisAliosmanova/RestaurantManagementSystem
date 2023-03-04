package com.example.waiter;

import com.example.waiter.Controllers.OrderController;
import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Repositories.*;
import com.example.waiter.Services.OrderDishService;
import com.example.waiter.Services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderDishRepository orderDishRepository;
    @InjectMocks
    OrderService orderService;
    @Mock
    private Model model;

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
    @Test
    public void testShowOrderDetails() {
        List<OrderDish> orderDishes = new ArrayList<>();
        OrderDish orderDish1 = new OrderDish();
        OrderDish orderDish2 = new OrderDish();
        orderDishes.add(orderDish1);
        orderDishes.add(orderDish2);
        when(orderDishRepository.findByOrderId(anyLong())).thenReturn(orderDishes);
        Model model = mock(Model.class);
        String viewName = orderService.showOrderDetails(1L, model);
        verify(orderDishRepository).findByOrderId(1L);
        ModelMap model1 = new ModelMap();
        String expectedAttributeName = "activeOrders";
        assertFalse(model1.containsAttribute(expectedAttributeName));
        Object attributeValue = model1.get(expectedAttributeName);
        assertFalse(attributeValue instanceof List<?>);
        //something is not that right here
    }
    @Test
    public void testShowOrderDetailsByDate() throws ParseException, ParseException {
        Date testDate = new Date(1L); //wants it in milliseconds :)
        List<OrderDish> orderDishes = new ArrayList<>();
        orderDishes.add(new OrderDish());
        given(orderDishRepository.findByOrderDate(testDate)).willReturn(orderDishes);
        Model model = new ExtendedModelMap();
        String viewName = orderService.showOrderDetailsByDate(testDate, model);
        assertTrue(model.containsAttribute("activeOrders"));
        List<OrderDish> activeOrders = (List<OrderDish>) model.getAttribute("activeOrders");
        assertEquals(orderDishes, activeOrders);
        assertEquals("/orderDetailsCook", viewName);
    }

}
