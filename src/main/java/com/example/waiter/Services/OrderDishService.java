package com.example.waiter.Services;

import com.example.waiter.Entities.Order;
import com.example.waiter.Entities.OrderDish;
import com.example.waiter.Exceptions.NoOrderDishException;
import com.example.waiter.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Service
public class OrderDishService {
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


    public String addOrderDish(Model model) {
        model.addAttribute("orderDish", new OrderDish());
        model.addAttribute("dishes", dishRepository.findAll());
        model.addAttribute("drinks", drinkRepository.findAll());
        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("orderId", orderRepository.findFirstByOrderByIdDesc());
        model.addAttribute("order", new Order());
        return ("/addOrderDish");
    }


    public ModelAndView addOrderDish(OrderDish orderDish, BindingResult bindingResult, Model model, boolean addAnotherDish) {
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
        order.setTotalPrice(priceDish + priceDrink + order.getTotalPrice());
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

    public String editOrderDetails(Model model){
        model.addAttribute("activeOrders", orderDishRepository.findAll());
        return "/editOrderDetails";
    }

    public ModelAndView deleteOrderDish(Long orderDishId, Model model ) {
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

    private static Long orderId;
    public double setOrderPriceUpdate(OrderDish orderDish) {
        double priceDish = 0;
        double priceDrink = 0;
        if (orderDish.getDish() != null) {
            priceDish = orderDish.getDish().getPrice() * orderDish.getDishCount();
        }
        if (orderDish.getDrink() != null) {
            priceDrink = orderDish.getDrink().getPrice() * orderDish.getDrinkCount();
        }
        Optional<Order> order = orderRepository.findById(orderId);
        order.get().setTotalPrice(priceDish + priceDrink);
        orderRepository.save(order.get());
        return order.get().getTotalPrice();
    }
    public String editOrderDish(Long orderDishId, Model model ) {
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
    public ModelAndView updateOrderDish(OrderDish orderDish, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/editOrderDish");
        } else {
            Optional<Order> order = orderRepository.findById(orderId);
            order.get().setTotalPrice(setOrderPriceUpdate(orderDish));
            System.out.println("Updated price " + order.get().getTotalPrice());
            orderDish.setOrder(order.get());
            orderDishRepository.save(orderDish);
            return new ModelAndView("redirect:/homePageWaiter");
        }
    }
    @GetMapping ("/addOrderDish")
    public String addDishDrinkToExistingOrder(Long orderDishId, Model model){
        Optional <OrderDish> optionalOrderDish=orderDishRepository.findById(orderDishId) ;
        model.addAttribute("orderId",optionalOrderDish.get().getOrder().getId());
        return "/addOrderDish";
    }
}
