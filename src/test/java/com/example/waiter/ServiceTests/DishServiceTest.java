package com.example.waiter.ServiceTests;

import com.example.waiter.Entities.Dish;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Enums.DishType;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Services.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishServiceTest {
    @InjectMocks
    DishService dishService;
    @Mock
    DishRepository dishRepository;
    @Mock
    OrderDishRepository orderDishRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddDishSubmit_WithBindingErrors() {
        Dish dish = new Dish();
        dish.setName("");
        dish.setIngredients("");
        dish.setId(1L);
        dish.setPrice(5);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ModelAndView result = dishService.addDishSubmit(dish, bindingResult);
        assertEquals("/restaurantMenu/addDish", result.getViewName());
    }
    @Test
    void testAddDishSubmit_WithoutBindingErrors() {
        Dish dish = new Dish();
        dish.setName("Cucumber soup");
        dish.setIngredients("cucumber, yogurt, garlic");
        dish.setId(1L);
        dish.setPrice(3);
        dish.setType(DishType.SOUP);
        System.out.println(dish.getName() + dish.getIngredients() + dish.getType() + dish.getId());
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        ModelAndView result = dishService.addDishSubmit(dish, bindingResult);
        verify(dishRepository, times(1)).save(dish);
        assertEquals("redirect:/homePageWaiter", result.getViewName());
    }
    @Test
    void testAddDish() {
        Model model = new ExtendedModelMap();
        String viewName = dishService.addDish(model);
        assertEquals("/restaurantMenu/addDish", viewName);
    }
    @Test
    void testDeleteDish() {
        Long id = 20L;
        Model model = new ExtendedModelMap();
        List<OrderDish> orderDishList = new ArrayList<>();
        when(orderDishRepository.findAll()).thenReturn(orderDishList);
        ModelAndView result = dishService.deleteDish(id, model);
        verify(dishRepository, times(1)).deleteById(id);
    }
    @Test
    void testEditDishWithExistingId() {
        Long dishId = 1L;
        Dish dish = new Dish();
        dish.setName("Cucumber soup");
        dish.setIngredients("cucumber, yogurt, garlic");
        dish.setId(1L);
        dish.setPrice(3);

        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));
        Model model = new ExtendedModelMap();
        String viewName = dishService.editDish(dishId, model);
        assertEquals("/restaurantMenu/editDish", viewName);
        assertEquals(dish, model.getAttribute("dish"));
        assertNull(model.getAttribute("errorMsg"));
    }
    @Test
    void testEditDishWithNonExistingId() {
        Long dishId = 1L;
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());
        Model model = new ExtendedModelMap();
        String viewName = dishService.editDish(dishId, model);
        assertEquals("/restaurantMenu/editDish", viewName);
        assertEquals("Error!", model.getAttribute("dish"));
        assertEquals(" Not existing dish with id = " + dishId, model.getAttribute("errorMsg"));
    }
    @Test
    void testUpdateDishWithValidDish() {
        Dish dish = new Dish();
        dish.setName("Cucumber soup");
        dish.setIngredients("cucumber, yogurt, garlic");
        dish.setId(1L);
        dish.setPrice(3);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        Model model = new ExtendedModelMap();
        ModelAndView modelAndView = dishService.updateDish(dish, bindingResult, model);
        verify(dishRepository, times(1)).save(dish);
        assertEquals("redirect:/restaurantMenu", modelAndView.getViewName());
    }
    @Test
    void testUpdateDishWithInvalidDish() {
        Dish dish = new Dish();
        dish.setName("Cucumber soup");
        dish.setIngredients("cucumber, yogurt, garlic");
        dish.setId(1L);
        dish.setPrice(3);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = new ExtendedModelMap();
        ModelAndView modelAndView = dishService.updateDish(dish, bindingResult, model);
        verify(dishRepository, never()).save(dish);
        assertEquals("/restaurantMenu/editDish", modelAndView.getViewName());
    }
}
