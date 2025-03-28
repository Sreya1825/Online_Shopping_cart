package com.shopcart.cartservice.entity;


import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class CartItems {

    @Id
    @GeneratedValue//(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;  // Reference to UserDto's user ID

    @NotNull(message = "Product ID cannot be null")
    private Long productId;  // Reference to ProductDto's product ID

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Product status cannot be null")
    private String productStatus;  // Reference to ProductDto's status

    // This field will be automatically calculated
    private BigDecimal cartValue;

    // Getters and Setters
}
