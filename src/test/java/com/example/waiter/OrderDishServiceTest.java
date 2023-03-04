package com.example.waiter;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Drink;
import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Exceptions.NoOrderDishException;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.waiter.Controllers.OrderDishController.orderId;
import static org.hamcrest.MatcherAssert.assertThat;
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

//    @Test
//    public void testCalculateTotalPriceDeleteMethod() {
//        // Create an order dish with some initial values
//        Order order = new Order();
//        order.setTotalPrice(50.0);
//        Dish dish = new Dish();
//        dish.setPrice(10.0);
//        Drink drink = new Drink();
//        drink.setPrice(5.0);
//        OrderDish orderDish = new OrderDish();
//        orderDish.setOrder(order);
//        orderDish.setDish(dish);
//        orderDish.setDishCount(2);
//        orderDish.setDrink(drink);
//        orderDish.setDrinkCount(1);
//
//        // Call the method being tested
//        double result = orderDishService.calculateTotalPriceDeleteMethod(orderDish);
//
//        // Check the expected result
//        double expected = 30.0;
//        assertEquals(expected, result, 0.0);
//    }

    @Test
    public void testDeleteOrderDish() {
        // Create an order dish with some initial values
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

        // Mock the repository method to return the order dish when findById is called
        when(orderDishRepository.findById(1L)).thenReturn(Optional.of(orderDish));
        Model model=new ExtendedModelMap();
        // Call the method being tested
        ModelAndView result = orderDishService.deleteOrderDish(1L, model);

        // Check the expected result
        ModelAndView expected = new ModelAndView("redirect:/editOrderDetails");
        assertEquals(expected.getViewName(), result.getViewName());
        assertEquals(expected.getModel(), result.getModel());

        // Check that the repository method was called with the expected argument
        verify(orderDishRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddOrderDishWithNoDishOrDrink() {
        // Create an order dish with null dish and drink fields
        OrderDish orderDish = new OrderDish();
        orderDish.setDrink(null);
        orderDish.setDish(null);
        Model model=new ExtendedModelMap();
        BindingResult bindingResult=mock(BindingResult.class);
        // Call the method being tested and expect an exception to be thrown
        assertThrows(NoOrderDishException.class, () -> orderDishService.addOrderDish(orderDish, bindingResult, model, false));
    }

//    @Test
//    public void testAddOrderDishWithErrors() {
//        // Set up the mock binding result to have errors
//        BindingResult bindingResult=mock(BindingResult.class);
//        when(bindingResult.hasErrors()).thenReturn(true);
//        Model model=new ExtendedModelMap();
//        // Call the method being tested and expect a ModelAndView object with the "/addOrderDish" view name
//        ModelAndView result = orderDishService.addOrderDish(new OrderDish(), bindingResult, model, false);
//        assertEquals("/addOrderDish", result.getViewName());
//
//        // Check that the expected model attributes were added
//        verify(model, times(1)).addAttribute(eq("dishes"), anyList());
//        verify(model, times(1)).addAttribute(eq("drinks"), anyList());
//        verify(model, times(1)).addAttribute(eq("orders"), anyList());
//    }

//    @Test
//    public void testAddOrderDishWithoutAddingAnotherDish() {
//        // Create a mock OrderDish object and set up the mock repository to return the latest order when findFirstByOrderByIdDesc is called
//        OrderDish orderDish = mock(OrderDish.class);
//        Order latestOrder = new Order();
//        when(orderDish.getOrder()).thenReturn(latestOrder);
//        when(orderRepository.findFirstByOrderByIdDesc()).thenReturn(latestOrder);
//        Model model=new ExtendedModelMap();
//        BindingResult bindingResult=mock(BindingResult.class);
//        // Call the method being tested and expect a ModelAndView object with the "redirect:/homePageWaiter" view name
//        ModelAndView result = orderDishService.addOrderDish(orderDish, bindingResult, model, false);
//        assertEquals("redirect:/homePageWaiter", result.getViewName());
//
//        // Check that the order dish was saved and that setOrderPrice and deleteNullOrders were called
//        verify(orderDishRepository, times(1)).save(orderDish);
//        verify(orderDishService, times(1)).setOrderPriceUpdate(orderDish);
//        //verify(orderDishService, times(1)).deleteNullOrders();
//    }
@Test
public void deleteNullOrdersTest() {
    List<Order> orders = new ArrayList<>();
    Order order=new Order();
    order.setId(1L);
    orders.add(order);
    when(orderRepository.findAll()).thenReturn(orders);
    orderDishService.deleteNullOrders();
    verify(orderRepository, times(1)).deleteById(1L);
}

    @Test
    void handleNoOrderDishException_shouldReturnErrorViewWithErrorMessage() {
        // create a new instance of NoOrderDishException with a custom message
        String errorMessage = "AT LEAST ONE DISH OR DRINK SHOULD BE SELECTED!";
        NoOrderDishException ex = new NoOrderDishException(errorMessage);

        // call the handleNoOrderDishException() method
        Model model = new ConcurrentModel();
        String viewName = orderDishService.handleNoOrderDishException(ex, model);

        // verify that the returned view name is equal to "error"
        assertEquals("error", viewName);

        // verify that the model contains the expected error message
        assertTrue(model.containsAttribute("error"));
        assertEquals(errorMessage, model.getAttribute("error"));
    }


}
