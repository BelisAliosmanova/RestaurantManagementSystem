package com.example.waiter;

import com.example.waiter.Controllers.RestaurantMenu;
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
//    @InjectMocks
//    private MockMvc mockMvc;
//    @Test
//    public void testGetAllDishes() throws Exception {
//        Iterable<Dish> dishes = Arrays.asList(new Dish(), new Dish());
//        Iterable<Drink> drinks = Arrays.asList(new Drink(), new Drink());
//        Mockito.when(dishRepository.findAll()).thenReturn(dishes);
//        Mockito.when(drinkRepository.findAll()).thenReturn(drinks);
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurantMenu"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("/restaurantMenu"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("allDishes"))
//                .andExpect(MockMvcResultMatchers.model().attribute("allDishes", dishes))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("allDrinks"))
//                .andExpect(MockMvcResultMatchers.model().attribute("allDrinks", drinks));
//    }
@Test
void getAllDishes_shouldReturnRestaurantMenuViewWithModelAttributes() {
    // create a new instance of the Model class
    Model model = new ConcurrentModel();

    // call the getAllDishes() method
    String viewName = restaurantMenuService.getAllDishes(model);

    // verify that the returned view name is equal to "/restaurantMenu"
    assertEquals("/restaurantMenu", viewName);

    // verify that the model contains the expected attributes and values
    assertTrue(model.containsAttribute("allDishes"));
    assertTrue(model.containsAttribute("allDrinks"));

    Iterable<Dish> expectedDishes = dishRepository.findAll();
    Iterable<Drink> expectedDrinks = drinkRepository.findAll();

    assertEquals(expectedDishes, model.getAttribute("allDishes"));
    assertEquals(expectedDrinks, model.getAttribute("allDrinks"));
}
}
