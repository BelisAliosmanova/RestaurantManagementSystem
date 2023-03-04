package com.example.waiter;

import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RestaurantMenuServiceTest {
    @Mock
    DishRepository dishRepository;
    @Mock
    DrinkRepository drinkRepository;
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
}
