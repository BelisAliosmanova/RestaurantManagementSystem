package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Exceptions.NoOrderDishException;
import com.example.waiter.Repositories.DishRepository;
import com.example.waiter.Repositories.DrinkRepository;
import com.example.waiter.Repositories.OrderDishRepository;
import com.example.waiter.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class OrderDishController {
    @Autowired
    private OrderDishRepository orderDishRepository;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DrinkRepository drinkRepository;

    @GetMapping("/addOrderDish")
    public String addOrderDish(Model model) {
        model.addAttribute("orderDish", new OrderDish());
        model.addAttribute("dishes", dishRepository.findAll());
        model.addAttribute("drinks", drinkRepository.findAll());
        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("orderId", orderRepository.findFirstByOrderByIdDesc());
        model.addAttribute("order", new Order());
        return ("/addOrderDish");
    }

    @PostMapping("/orderSubmitDish")
    public ModelAndView addOrderDish(@Valid OrderDish orderDish, BindingResult bindingResult, Model model,
                                     @RequestParam(value = "addAnotherDish", required = false) boolean addAnotherDish) {
        if (orderDish.getDrink() == null && orderDish.getDish() == null) {
            throw new NoOrderDishException("AT LEAST ONE DISH OR DRINK SHOULD BE SELECTED!");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("dishes", dishRepository.findAll());
            model.addAttribute("drinks", drinkRepository.findAll());
            model.addAttribute("orders", orderRepository.findAll());
            return new ModelAndView("/addOrderDish");
        } else if (!addAnotherDish) {
            orderDish.setOrder(orderRepository.findFirstByOrderByIdDesc());
            orderDishRepository.save(orderDish);
            setOrderRepository(orderDish);
            deleteNullOrders();
            return new ModelAndView("redirect:/homePageWaiter");
        } else {
            model.addAttribute("orderId", orderRepository.findFirstByOrderByIdDesc());
            orderDish.setOrder(orderRepository.findFirstByOrderByIdDesc());
            orderDishRepository.save(orderDish);
            setOrderRepository(orderDish);
            deleteNullOrders();
            return new ModelAndView("redirect:/addOrderDish");
        }
    }

    public double setOrderRepository(OrderDish orderDish) {
        double priceDish = 0;
        double priceDrink = 0;
        if (orderDish.getDish() != null) {
            priceDish = orderDish.getDish().getPrice() * orderDish.getDishCount();
        }
        if (orderDish.getDrink() != null) {
            priceDrink = orderDish.getDrink().getPrice() * orderDish.getDrinkCount();
        }
        Order order = orderDish.getOrder();
        order.setTotalPrice(priceDish + priceDrink + order.getTotalPrice());
        orderRepository.save(order);
        return priceDish;
    }
    public void deleteNullOrders(){
        Iterable<Order> orders = orderRepository.findAll();
        for (Order order: orders) {
            if(order.getTotalPrice()==0){
                orderRepository.deleteById(order.getId());
            }
        }
    }
    @ExceptionHandler(NoOrderDishException.class)
    @GetMapping("/error")
    public String handleNoOrderDishException(NoOrderDishException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
    @GetMapping ("/editOrder/{orderId}")
    public String editOrder(@PathVariable(name="orderId") Long orderId, Model model ) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            model.addAttribute("order", optionalOrder.get());
        } else {
            model.addAttribute("order", "Error!");
            model.addAttribute("errorMsg", "Not existing order with id: " + orderId);
        }
        return "/editOrder";
    }
    @PostMapping("/updateOrder")
    public ModelAndView updateOrder(@Valid Order order, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/editOrder");
        } else {
            if(order.getStatus().equals(OrderStatus.PAID)){
                for (OrderDish orderDish:orderDishRepository.findAll()) {
                    if(orderDish.getOrder().getId().equals(order.getId())){
                        model.addAttribute("orderDish", orderDish);
                        model.addAttribute("order", order);
                    }
                }
                orderRepository.save(order);
                return new ModelAndView("/orderSummary");
            }
            orderRepository.save(order);
            return new ModelAndView("redirect:/activeOrders");
        }
    }
}
