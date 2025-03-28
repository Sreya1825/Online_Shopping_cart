package com.shopcart.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "OrderTable")
public class Order {

    @Id
    @GeneratedValue
    private Long orderId;   // Unique ID for the order

    @NotNull(message = "Cart ID cannot be null")
    private Long cartId;    // Reference to Cart

    @NotNull(message = "Cart value cannot be null")
    private BigDecimal cartValue;  // Value of the cart

    @NotNull(message = "Order payment status cannot be null")
    private String orderPaymentStatus;  // Payment status (PENDING or SUCCESSFUL)

    
}
