//package com.example.waiter.Entities;
//
//import javax.persistence.*;
//import java.util.Optional;
//
//@Entity
//public class OrderDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id")
//    private Long orderId;
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "dish_id")
//    private Long dishId;
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "drink_id")
//    private Long drinkId;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(Long orderId) {
//        this.orderId = orderId;
//    }
//
//    public Long getDishId() {
//        return dishId;
//    }
//
//    public void setDishId(Long dishId) {
//        this.dishId = dishId;
//    }
//
//    public Long getDrinkId() {
//        return drinkId;
//    }
//
//    public void setDrinkId(Long drinkId) {
//        this.drinkId = drinkId;
//    }
//}
