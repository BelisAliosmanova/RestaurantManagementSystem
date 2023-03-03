package com.example.waiter;

import com.example.waiter.Entities.Drink;
import com.example.waiter.Repositories.DrinkRepository;
import com.example.waiter.Services.DrinkService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class DrinkServiceTest {
    @InjectMocks
    DrinkService drinkService;
    @Mock
    DrinkRepository drinkRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddDrinkSubmit_WithBindingErrors() {
        Drink drink = new Drink();
        drink.setName("");
        drink.setId(1L);
        drink.setPrice(5);
        drink.setPrice(-1);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ModelAndView result = drinkService.addDrink(drink, bindingResult);
        assertEquals("/addDrink", result.getViewName());
    }
    @Test
    void testAddDrinkSubmit_WithoutBindingErrors() {
        Drink drink = new Drink();
        drink.setName("");
        drink.setId(1L);
        drink.setPrice(5);
        drink.setPrice(-1);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        ModelAndView result = drinkService.addDrink(drink, bindingResult);
        verify(drinkRepository, times(1)).save(drink);
        assertEquals("redirect:/homePageWaiter", result.getViewName());
    }
    @Test
    void testAddDrink() {
        Model model = new ExtendedModelMap();
        String viewName = drinkService.addDrink(model);
        assertEquals("/addDrink", viewName);
    }
    @Test
    void testDeleteDrink() {
        Long id = 20L;
        ModelAndView result = drinkService.deleteDrink(id);
        verify(drinkRepository, times(1)).deleteById(id);
    }
    @Test
    void testEditDrinkWithExistingId() {
        Long drinkId = 1L;
        Drink drink = new Drink();
        drink.setName("");
        drink.setId(1L);
        drink.setPrice(5);
        drink.setPrice(-1);
        when(drinkRepository.findById(drinkId)).thenReturn(Optional.of(drink));
        Model model = new ExtendedModelMap();
        String viewName = drinkService.editDrink(drinkId, model);
        assertEquals("/editDrink", viewName);
        assertEquals(drink, model.getAttribute("drink"));
        assertNull(model.getAttribute("errorMsg"));
    }
    @Test
    void testEditDrinkWithNonExistingId() {
        Long drinkId = 1L;
        when(drinkRepository.findById(drinkId)).thenReturn(Optional.empty());
        Model model = new ExtendedModelMap();
        String viewName = drinkService.editDrink(drinkId, model);
        assertEquals("/editDrink", viewName);
        assertEquals("Error!", model.getAttribute("drink"));
        assertEquals(" Not existing drink with id = " + drinkId, model.getAttribute("errorMsg"));
    }
    @Test
    void testUpdateDrinkWithValidDrink() {
        Drink drink = new Drink();
        drink.setName("");
        drink.setId(1L);
        drink.setPrice(5);
        drink.setPrice(-1);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        Model model = new ExtendedModelMap();
        ModelAndView modelAndView = drinkService.updateDrink(drink, bindingResult, model);
        verify(drinkRepository, times(1)).save(drink);
        assertEquals("redirect:/restaurantMenu", modelAndView.getViewName());
    }
    @Test
    void testUpdateDrinkWithInvalidDrink() {
        Drink drink = new Drink();
        drink.setName("");
        drink.setId(1L);
        drink.setPrice(5);
        drink.setPrice(-1);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = new ExtendedModelMap();
        ModelAndView modelAndView = drinkService.updateDrink(drink, bindingResult, model);
        verify(drinkRepository, never()).save(drink);
        assertEquals("/editDrink", modelAndView.getViewName());
    }
}
