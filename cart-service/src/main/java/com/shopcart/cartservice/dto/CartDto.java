package com.shopcart.cartservice.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartDto {
	

    private Long cartId;   // Unique ID of the cart item
    private Long userId;   // ID of the user (from UserDto)
    private int quantity;   // Quantity of the product in the cart
    private BigDecimal cartValue;  // Calculated cart value (price * quantity)
    private String productStatus;


}
