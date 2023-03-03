package com.example.waiter;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Drink;
import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Repositories.OrderRepository;
import com.example.waiter.Services.OrderDishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.waiter.Controllers.OrderDishController.orderId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderDishServiceTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderDishService orderDishService;
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
}
