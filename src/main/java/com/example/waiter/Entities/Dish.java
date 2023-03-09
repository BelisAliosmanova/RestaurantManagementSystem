package com.example.waiter.Entities;

import com.example.waiter.Enums.DishType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    @Size(min=3, max=50, message = "The name of the dish must be between 2 to 50 letters!")
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The type of the dish cannot be blank!")
    private DishType type;
    @NotNull
    @Size(min=3, max=50, message = "Please type the necessary ingredients!")
    private String ingredients;
    @NotNull
    @Min(value = 1, message = "In our restaurant the minimum required price is 1!")
    private double price;

    public String getIngredients() {
        return ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishType getType() {
        return type;
    }

    public void setType(DishType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
