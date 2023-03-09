package com.example.waiter.Entities;

import com.example.waiter.Enums.Role;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    @Size(min=5, max=50, message = "The username must be between 5 to 50 symbols!")
    private String username;
    @NotNull
    @Pattern(regexp="^(?=.*[a-zA-Z])(?=.*\\d).{5,}$", message = ("The password must be least 5 symbols, including numbers and letters!"))
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;
    @NotNull
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
