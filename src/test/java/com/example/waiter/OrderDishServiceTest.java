package com.example.waiter;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Drink;
import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Services.OrderDishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.waiter.Controllers.OrderDishController.orderId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDishServiceTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderDishService orderDishService;
    @Mock
    DishRepository dishRepository;
    @Mock
    DrinkRepository drinkRepository;
    @Mock
    OrderDishRepository orderDishRepository;
    @Test
    public void testSetOrderPriceUpdate() {
        Dish dish = new Dish();
        dish.setPrice(10.0);
        Drink drink = new Drink();
        drink.setPrice(10.0);
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        OrderDish orderDish = new OrderDish();
        orderDish.setDish(dish);
        orderDish.setDrink(drink);
        orderDish.setDishCount(2);
        orderDish.setDrinkCount(1);
        double expectedPrice = 30.0;
        double actualPrice = orderDishService.setOrderPriceUpdate(orderDish);
        assertEquals(expectedPrice, actualPrice);
        Mockito.verify(orderRepository, times(1)).findById(orderId);
        Mockito.verify(orderRepository, times(1)).save(order);
    }
    @Test
    void addDishDrinkToExistingOrder_ShouldReturnAddOrderDishViewAndModelAttributes() {
        Long orderDishId = 1L;
        OrderDish orderDish = new OrderDish();
        orderDish.setOrder(new Order());
        Mockito.when(orderDishRepository.findById(orderDishId)).thenReturn(Optional.of(orderDish));
        Model model = new ExtendedModelMap();
        String viewName = orderDishService.addDishDrinkToExistingOrder(orderDishId, model);
        assertEquals("/addOrderDish", viewName);
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
        assertEquals("/editOrderDish", viewName);
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
        assertEquals("/editOrderDetails", viewName);
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
//    @Test
//    void testEditOrderStatusCook_existingOrder() {
//        // Arrange
//        Long orderId = 1L;
//        Order order = new Order();
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//        Model model = new ExtendedModelMap();
//        // Act
//        String result = orderDishService.editOrderStatusCook(orderId, model);
//
//        // Assert
//        assertEquals("/editOrderStatusCook", result);
//        verify(model).addAttribute("order", order);
//        verifyNoMoreInteractions(model);
//    }
}
