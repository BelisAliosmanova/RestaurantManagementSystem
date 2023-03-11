package com.example.waiter.ServiceTests;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Drink;
import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Exceptions.NoOrderDishException;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Services.OrderDishService;
import com.example.waiter.Services.OrderService;
import com.example.waiter.Services.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDishServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderService orderService;
    @InjectMocks
    OrderDishService orderDishService;
    @Mock
    DishRepository dishRepository;
    @Mock
    DrinkRepository drinkRepository;
    @Mock
    OrderDishRepository orderDishRepository;
    @InjectMocks
    StaffService staffService;
    @Test
    void addDishDrinkToExistingOrder_ShouldReturnAddOrderDishViewAndModelAttributes() {
        Long orderDishId = 1L;
        OrderDish orderDish = new OrderDish();
        orderDish.getId();
        orderDish.setOrder(new Order());
        Mockito.when(orderDishRepository.findById(orderDishId)).thenReturn(Optional.of(orderDish));
        Model model = new ExtendedModelMap();
        String viewName = orderDishService.addDishDrinkToExistingOrder(orderDishId, model);
        assertEquals("/order/addOrderDish", viewName);
        assertEquals(orderDish.getOrder().getId(), model.getAttribute("orderId"));
    }

    @Test
    void testUpdateOrderDishHasErrors() {
        OrderDish orderDish = new OrderDish();
        orderDish.setId(1L);
        Order order = new Order();
        order.setId(1L);
        order.setTotalPrice(-10);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = new ExtendedModelMap();
        ModelAndView modelAndView = orderDishService.updateOrderDish(orderDish, bindingResult);
        verify(orderDishRepository, times(0)).save(orderDish);
        assertEquals("/editOrderDish", modelAndView.getViewName());
    }

    @Test
    void editOrderDish_shouldReturnEditOrderDishViewAndModelAttributes() {
        Long orderDishId = 1L;
        Model model = new ConcurrentModel();
        OrderDish orderDish = new OrderDish();
        orderDish.setId(orderDishId);
        orderDish.setOrder(new Order());
        given(orderDishRepository.findById(orderDishId)).willReturn(Optional.of(orderDish));
        String viewName = orderDishService.editOrderDish(orderDishId, model);
        assertEquals("/order/editOrderDish", viewName);
        assertEquals(orderDish.getOrder().getId(), model.getAttribute("orderId"));
        assertEquals(dishRepository.findAll(), model.getAttribute("dishes"));
        assertEquals(drinkRepository.findAll(), model.getAttribute("drinks"));
        assertEquals(orderDish, model.getAttribute("orderDish"));
    }

    @Test
    public void testEditOrderDetails() {
        List<OrderDish> orderDishList = new ArrayList<>();
        when(orderDishRepository.findAll()).thenReturn(orderDishList);
        Model model = new ExtendedModelMap();
        String viewName = orderDishService.editOrderDetails(model);
        assertEquals("/order/editOrderDetails", viewName);
    }

    @Test
    void testUpdateOrderDishWithInvalidDish() {
        OrderDish orderDish = new OrderDish();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = new ExtendedModelMap();
        ModelAndView modelAndView = orderDishService.updateOrderDish(orderDish, bindingResult);
        verify(orderDishRepository, never()).save(orderDish);
        assertEquals("/editOrderDish", modelAndView.getViewName());
    }
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testAddOrderDish() {
        Model model = new ExtendedModelMap();
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish());
        dishes.add(new Dish());
        List<Drink> drinks = new ArrayList<>();
        drinks.add(new Drink());
        drinks.add(new Drink());
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        when(dishRepository.findAll()).thenReturn(dishes);
        when(drinkRepository.findAll()).thenReturn(drinks);
        when(orderRepository.findAll()).thenReturn(orders);
        String result = orderDishService.addOrderDish(model);
        OrderDish orderDish = (OrderDish) model.getAttribute("orderDish");
        List<Dish> modelDishes = (List<Dish>) model.getAttribute("dishes");
        List<Drink> modelDrinks = (List<Drink>) model.getAttribute("drinks");
        List<Order> modelOrders = (List<Order>) model.getAttribute("orders");
        Long orderId = (Long) model.getAttribute("orderId");
        Order order = (Order) model.getAttribute("order");
        assertAll("model",
                () -> assertNotNull(orderDish),
                () -> assertEquals(dishes, modelDishes),
                () -> assertEquals(drinks, modelDrinks),
                () -> assertEquals(orders, modelOrders),
                () -> assertNotNull(order)
        );
        assertEquals("/order/addOrderDish", result);
    }
    @Test
    public void testDeleteOrderDish() {
        Order order = new Order();
        order.setTotalPrice(50.0);
        Dish dish = new Dish();
        dish.setPrice(10.0);
        Drink drink = new Drink();
        drink.setPrice(5.0);
        OrderDish orderDish = new OrderDish();
        orderDish.setId(1L);
        orderDish.setOrder(order);
        orderDish.setDish(dish);
        orderDish.setDishCount(2);
        orderDish.setDrink(drink);
        orderDish.setDrinkCount(1);
        when(orderDishRepository.findById(1L)).thenReturn(Optional.of(orderDish));
        Model model = new ExtendedModelMap();
        ModelAndView result = orderDishService.deleteOrderDish(1L, model);
        ModelAndView expected = new ModelAndView("redirect:/editOrderDetails");
        assertEquals(expected.getViewName(), result.getViewName());
        assertEquals(expected.getModel(), result.getModel());
        verify(orderDishRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddOrderDishWithNoDishOrDrink() {
        OrderDish orderDish = new OrderDish();
        orderDish.setDrink(null);
        orderDish.setDish(null);
        Model model = new ExtendedModelMap();
        BindingResult bindingResult = mock(BindingResult.class);
        assertThrows(NoOrderDishException.class, () -> orderDishService.addOrderDish(orderDish, bindingResult, model, false));
    }
    @Test
    public void deleteNullOrdersTest() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(1L);
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);
        orderDishService.deleteNullOrders();
        verify(orderRepository, times(1)).deleteById(1L);
    }
    @Test
    void handleNoOrderDishException_shouldReturnErrorViewWithErrorMessage() {
        String errorMessage = "AT LEAST ONE DISH OR DRINK SHOULD BE SELECTED!";
        NoOrderDishException ex = new NoOrderDishException(errorMessage);
        Model model = new ConcurrentModel();
        String viewName = orderDishService.handleNoOrderDishException(ex, model);
        assertEquals("error", viewName);
        assertTrue(model.containsAttribute("error"));
        assertEquals(errorMessage, model.getAttribute("error"));
    }
    @Test
    void testSetOrderPrice() {
        Drink drink = new Drink();
        drink.setPrice(2.50);
        drink.setName("Coke");
        Dish dish = new Dish();
        dish.setPrice(15.50);
        dish.setName("Pizza");
        Order order = new Order();
        order.setTotalPrice(0);
        OrderDish orderDish = new OrderDish();
        orderDish.setOrder(order);
        orderDish.setDrink(drink);
        orderDish.setDrinkCount(2);
        orderDish.setDish(dish);
        orderDish.setDishCount(1);
        when(orderRepository.save(order)).thenReturn(order);
        double actualTotalPrice = orderDishService.setOrderPrice(orderDish);
        double expectedTotalPrice = drink.getPrice() * orderDish.getDrinkCount()
                + dish.getPrice() * orderDish.getDishCount();
        assertEquals(expectedTotalPrice, actualTotalPrice);
        verify(orderRepository, times(1)).save(order);
    }
}
