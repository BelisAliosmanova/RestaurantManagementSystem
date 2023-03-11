package com.example.waiter.ServiceTests;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.Drink;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import com.example.waiter.Services.RestaurantMenuService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RestaurantMenuServiceTest {
    @Mock
    DishRepository dishRepository;
    @Mock
    DrinkRepository drinkRepository;
    @InjectMocks
    RestaurantMenuService restaurantMenuService;
    @Test
    void getAllDishes_shouldReturnRestaurantMenuViewWithModelAttributes() {

        Model model = new ConcurrentModel();
        String viewName = restaurantMenuService.getAllDishes(model);
        assertEquals("/restaurantMenu/restaurantMenu", viewName);
        assertTrue(model.containsAttribute("allDishes"));
        assertTrue(model.containsAttribute("allDrinks"));
        Iterable<Dish> expectedDishes = dishRepository.findAll();
        Iterable<Drink> expectedDrinks = drinkRepository.findAll();
        assertEquals(expectedDishes, model.getAttribute("allDishes"));
        assertEquals(expectedDrinks, model.getAttribute("allDrinks"));
    }
}
