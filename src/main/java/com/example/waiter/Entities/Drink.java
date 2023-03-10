package com.example.waiter.Entities;
import com.example.waiter.Enums.DrinkType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Entity
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    @Size(min=3, max=50, message = "The name of the drink must be between 2 to 50 letters!")
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The type of the drink cannot be blank!")
    private DrinkType type;
    @NotNull
    @Min(value = 1, message = "In our restaurant the minimum required price is 1!")
    private double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DrinkType getType() {
        return type;
    }

    public void setType(DrinkType type) {
        this.type = type;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
