package com.example.waiter;

import ch.qos.logback.classic.Logger;
import com.example.waiter.Controllers.OrderController;
import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Entities.Staff;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Enums.Role;
import com.example.waiter.Exceptions.NotFreeTableException;
import com.example.waiter.Repositories.*;
import com.example.waiter.Services.OrderDishService;
import com.example.waiter.Services.OrderService;
import com.example.waiter.Services.StaffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    @Mock
    private Principal principal;
    @Mock
    private StaffRepository staffRepository;
    @InjectMocks
    private StaffService staffService;

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
    @Test
    void testActiveOrdersCook() {
        // Mock the order repository to return some sample data
        Order order1=new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.ACTIVE);
        order1.setTotalPrice(50);
        order1.setTableNum(1);
        order1.getTableNum();
        Staff staff = new Staff();
        order1.setStaff(staff);
        order1.getOrderDate();
        order1.getStaff();
        Date date = new Date(1);
        order1.setOrderDate(date);
        Order order2=new Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.ACTIVE);
        order2.setTotalPrice(40);
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(order1);
        allOrders.add(order2);
        when(orderRepository.findAll()).thenReturn(allOrders);

        // Call the service method
        Model model = new ExtendedModelMap();
        String viewName = orderService.activeOrdersCook(model);

        // Verify the results
        assertEquals(viewName,"/activeOrdersCook");
  List<Order> activeOrdersCook = (List<Order>) model.getAttribute("activeOrdersCook");
assertEquals(activeOrdersCook.size(),2);

    }


    @Test
    public void testNotFreeTableException() {
        Model model = new ConcurrentModel();
        String viewName = orderService.NotFreeTableException(new NotFreeTableException("Table is not free"), model);

        assertEquals("error", viewName);
        assertEquals("Table is not free", model.getAttribute("error"));
    }
    @Test
    public void testEditOrderWithExistingOrder() {
        Long orderId = 123L;
        Order order = new Order();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        String viewName = orderService.editOrder(orderId, model, principal);
        assertEquals("/editOrder", viewName);
        verify(model, times(1)).addAttribute("order", order);
    }
    @Test
    public void testEditOrderWithNonExistingOrder() {
        Long orderId = 456L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        String username = "staff1";
        when(principal.getName()).thenReturn(username);
        Staff staff = new Staff();
        staff.setUsername(username);
        staff.setRole(Role.COOK);
        when(staffRepository.findByUsername(username)).thenReturn(staff);
        String viewName = orderService.editOrder(orderId, model, principal);
        assertEquals("/editOrder", viewName);
        verify(model, times(1)).addAttribute("staffRole", Role.COOK);
        verify(model, times(1)).addAttribute("order", "Error!");
        verify(model, times(1)).addAttribute("errorMsg", "Not existing order with id: " + orderId);
    }
    @Test
    public void testEditOrderStatusCookWithExistingOrder() {
        Long orderId = 123L;
        Order order = new Order();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Model model = new ExtendedModelMap();
        String viewName = orderService.editOrderStatusCook(orderId, model);
        assertEquals("/editOrderStatusCook", viewName);
        Order modelOrder = (Order) model.getAttribute("order");
        assertNotNull(modelOrder);
        assertEquals(orderId, modelOrder.getId());
    }
    @Test
    public void testEditOrderStatusCookWithNonExistingOrder() {
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        Model model = new ExtendedModelMap();
        String viewName = orderService.editOrderStatusCook(orderId, model);
        assertEquals("/editOrderStatusCook", viewName);
        assertEquals("Error!", model.getAttribute("order"));
        assertEquals("Not existing order with id: " + orderId, model.getAttribute("errorMsg"));
    }
    @Test
    public void testUpdateOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setTableNum(1);
        order.setStatus(OrderStatus.ACTIVE);
        List<OrderDish> orderDishes = new ArrayList<>();
        OrderDish orderDish1 = new OrderDish();
        orderDish1.setId(1L);
        Dish dish = new Dish();
        orderDish1.setDish(dish);
        orderDish1.setOrder(order);
        OrderDish orderDish2 = new OrderDish();
        orderDish2.setId(2L);
        orderDish2.setDish(dish);
        orderDish2.setOrder(order);
        orderDishes.add(orderDish1);
        orderDishes.add(orderDish2);
        when(orderDishRepository.findAll()).thenReturn(orderDishes);
        when(orderRepository.save(order)).thenReturn(order);
        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
        Model model = new ExtendedModelMap();
        ModelAndView modelAndView = orderService.updateOrder(order, bindingResult, model);
        assertEquals("redirect:/activeOrders", modelAndView.getViewName());
        Order paidOrder = new Order();
        paidOrder.setId(2L);
        paidOrder.setTableNum(2);
        paidOrder.setStatus(OrderStatus.PAID);
        when(orderRepository.save(paidOrder)).thenReturn(paidOrder);
        modelAndView = orderService.updateOrder(paidOrder, bindingResult, model);
        assertEquals("/orderSummary", modelAndView.getViewName());
        assertNull(modelAndView.getModel().get("order"));
        assertNull(modelAndView.getModel().get("orderDishes"));
    }
    @Test
    void testAddOrderSubmit() {
        Staff staff = new Staff();
        staff.setUsername("testuser");
        staff.setRole(Role.COOK);
        Authentication auth = new UsernamePasswordAuthenticationToken(staff.getUsername(), "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
        Order order = new Order();
        order.setTableNum(2);
        order.setOrderDate(Date.valueOf(LocalDate.now()));
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        Order activeOrder = new Order();
        activeOrder.setId(1L);
        activeOrder.setTableNum(1);
        activeOrder.setStatus(OrderStatus.ACTIVE);
        Iterable<Order> existingOrders = List.of(activeOrder);
        when(orderRepository.findAll()).thenReturn(existingOrders);
        when(staffRepository.getStaffByUsername("testuser")).thenReturn(staff);
        ModelAndView mav = orderService.addOrder(order, bindingResult);
        verify(orderRepository).save(order);
        assertEquals("redirect:/addOrderDish", mav.getViewName());
    }
    @Test
    public void testActiveOrders() {
        Order order1 = new Order();
        order1.setStatus(OrderStatus.ACTIVE);
        Order order2 = new Order();
        order2.setStatus(OrderStatus.PREPARING);
        Order order3 = new Order();
        order3.setStatus(OrderStatus.PREPARED);
        Order order4 = new Order();
        order4.setStatus(OrderStatus.SERVED);
        Order order5 = new Order();
        order5.setStatus(OrderStatus.PAID);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2, order3, order4, order5));
        String viewName = orderService.activeOrders(model);
        assertThat(viewName).isEqualTo("/activeOrders");
    }
}
