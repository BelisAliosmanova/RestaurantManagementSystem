package com.example.waiter.Controllers;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Entities.Staff;
import com.example.waiter.Enums.OrderStatus;
import com.example.waiter.Exceptions.NoOrderDishException;
import com.example.waiter.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private StaffRepository staffRepository;

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
            setOrderPrice(orderDish);
            deleteNullOrders();
            return new ModelAndView("redirect:/homePageWaiter");
        } else {
            model.addAttribute("orderId", orderRepository.findFirstByOrderByIdDesc());
            orderDish.setOrder(orderRepository.findFirstByOrderByIdDesc());
            orderDishRepository.save(orderDish);
            setOrderPrice(orderDish);
            deleteNullOrders();
            return new ModelAndView("redirect:/addOrderDish");
        }
    }

    private double setOrderPrice(OrderDish orderDish) {
        double priceDish = 0;
        double priceDrink = 0;
        if (orderDish.getDish() != null) {
            priceDish = orderDish.getDish().getPrice() * orderDish.getDishCount();
        }
        if (orderDish.getDrink() != null) {
            priceDrink = orderDish.getDrink().getPrice() * orderDish.getDrinkCount();
        }
        Order order = orderDish.getOrder();
        System.out.println("before method - " + order.getTotalPrice());
        order.setTotalPrice(priceDish + priceDrink + order.getTotalPrice());
        System.out.println("from method - "+order.getTotalPrice());
        orderRepository.save(order);
        return order.getTotalPrice();
    }
    private void deleteNullOrders(){
        Iterable<Order> orders = orderRepository.findAll();
        for (Order order: orders) {
            if(order.getTotalPrice() < 1){
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
    public String editOrder(@PathVariable(name="orderId") Long orderId, Model model, Principal principal) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            model.addAttribute("order", optionalOrder.get());
        } else {
            String username = principal.getName();
            Staff staff = staffRepository.findByUsername(username);
            model.addAttribute("staffRole", staff.getRole());
            model.addAttribute("order", "Error!");
            model.addAttribute("errorMsg", "Not existing order with id: " + orderId);
        }
        return "/editOrder";
    }
    @PostMapping("/updateOrder")
    public ModelAndView updateOrder(@Valid Order order, BindingResult bindingResult, Model model) {
        List<OrderDish> orderDishes = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/editOrder");
        } else {
            if(order.getStatus().equals(OrderStatus.PAID)){
                for (OrderDish orderDish:orderDishRepository.findAll()) {
                    if(orderDish.getOrder().getId().equals(order.getId())){
                        orderDishes.add(orderDish);
                    }
                }
                model.addAttribute("order", order);
                model.addAttribute("orderDishes", orderDishes);
                orderRepository.save(order);
                return new ModelAndView("/orderSummary");
            }
            orderRepository.save(order);
            return new ModelAndView("redirect:/activeOrders");
        }
    }
    @GetMapping("/editOrderDetails")
    public String editOrderDetails(Model model){
        model.addAttribute("activeOrders", orderDishRepository.findAll());
        return "/editOrderDetails";
    }
    @PostMapping ("/delete/{orderDishId}")
    public ModelAndView deleteOrderDish(@PathVariable (name="orderDishId") Long orderDishId, Model model ) {
        Optional<OrderDish> orderDish = orderDishRepository.findById(orderDishId);
        orderDish.get().getOrder().setTotalPrice(calculateTotalPriceDeleteMethod(orderDish.get()));
        orderDishRepository.deleteById(orderDishId);
        return new ModelAndView("redirect:/editOrderDetails");
    }
    private double calculateTotalPriceDeleteMethod(OrderDish orderDish){
        double currentPrice = orderDish.getOrder().getTotalPrice();
        if(orderDish.getDishCount()!=0){
            currentPrice -= (orderDish.getDishCount()*orderDish.getDish().getPrice());
        }
        if(orderDish.getDrinkCount()!=0){
            currentPrice -= (orderDish.getDrinkCount()*orderDish.getDrink().getPrice());
        }
        System.out.println(currentPrice);
        return currentPrice;
    }

    public static Long orderId;
    @GetMapping ("/editOrderDish/{orderDishId}")
    public String editOrderDish(@PathVariable (name="orderDishId") Long orderDishId, Model model ) {
        Optional<OrderDish> optionalOrderDish = orderDishRepository.findById(orderDishId);
        if (optionalOrderDish.isPresent()) {
            orderId = optionalOrderDish.get().getOrder().getId();
            model.addAttribute("dishes", dishRepository.findAll());
            model.addAttribute("drinks", drinkRepository.findAll());
            model.addAttribute("orderDish", optionalOrderDish.get());
        } else {
            model.addAttribute("orderDish", "Error!");
            model.addAttribute("errorMsg", "Not existing order with id: " + orderDishId);
        }
        return "/editOrderDish";
    }
    @PostMapping("/updateOrderDish")
    public ModelAndView updateOrderDish(@Valid OrderDish orderDish, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/editOrderDish");
        } else {
            Optional<Order> order = orderRepository.findById(orderId);
            orderDish.setOrder(order.get());
            orderDishRepository.save(orderDish);
            return new ModelAndView("redirect:/homePageWaiter");
        }
    }
}
