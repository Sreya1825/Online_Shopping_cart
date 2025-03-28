package com.shopcart.carttest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.shopcart.cartservice.controller.CartController;
import com.shopcart.cartservice.entity.CartItems;
import com.shopcart.cartservice.exception.CustomException;
import com.shopcart.cartservice.service.CartService;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    private CartItems cartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartItem = new CartItems();
        cartItem.setCartId(1L);
        cartItem.setProductId(1L);
        cartItem.setQuantity(2);
        cartItem.setUserId(1L);
        cartItem.setProductStatus("Available");
        cartItem.setCartValue(new BigDecimal("200.00"));
    }

    @Test
    public void testGetAllCartList() {
        when(cartService.getAllCartList()).thenReturn(Collections.singletonList(cartItem));

        ResponseEntity<List<CartItems>> response = cartController.getAllCartList();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(cartItem, response.getBody().get(0));
        verify(cartService, times(1)).getAllCartList();
    }

    @Test
    public void testGetCartById_Success() {
        when(cartService.getcartById(1L)).thenReturn(java.util.Optional.of(cartItem));

        ResponseEntity<CartItems> response = cartController.getCartById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItem, response.getBody());
        verify(cartService, times(1)).getcartById(1L);
    }

    @Test
    public void testGetCartById_NotFound() {
        when(cartService.getcartById(1L)).thenReturn(java.util.Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> cartController.getCartById(1L));
        assertEquals("Cart not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartService, times(1)).getcartById(1L);
    }

    @Test
    public void testGetCartByUserId_Success() {
        when(cartService.getCartByUserId(1L)).thenReturn(Collections.singletonList(cartItem));

        ResponseEntity<List<CartItems>> response = cartController.getCartByUserId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(cartItem, response.getBody().get(0));
        verify(cartService, times(1)).getCartByUserId(1L);
    }

    @Test
    public void testGetCartByUserId_NotFound() {
        when(cartService.getCartByUserId(1L)).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> cartController.getCartByUserId(1L));
        assertEquals("No carts found for user ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartService, times(1)).getCartByUserId(1L);
    }

    @Test
    public void testGetCartByProductId_Success() {
        when(cartService.getCartByProductId(1L)).thenReturn(Collections.singletonList(cartItem));

        ResponseEntity<List<CartItems>> response = cartController.getCartByProductId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(cartItem, response.getBody().get(0));
        verify(cartService, times(1)).getCartByProductId(1L);
    }

    @Test
    public void testGetCartByProductId_NotFound() {
        when(cartService.getCartByProductId(1L)).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> cartController.getCartByProductId(1L));
        assertEquals("No carts found for product ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartService, times(1)).getCartByProductId(1L);
    }

    @Test
    public void testAddToCart() {
        when(cartService.addToCart(any(CartItems.class))).thenReturn(cartItem);

        ResponseEntity<CartItems> response = cartController.addToCart(cartItem);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItem, response.getBody());
        verify(cartService, times(1)).addToCart(any(CartItems.class));
    }

    @Test
    public void testUpdateCart_Success() {
        when(cartService.updateCart(1L, cartItem)).thenReturn(java.util.Optional.of(cartItem));

        ResponseEntity<CartItems> response = cartController.updateCart(1L, cartItem);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItem, response.getBody());
        verify(cartService, times(1)).updateCart(1L, cartItem);
    }

    @Test
    public void testUpdateCart_NotFound() {
        when(cartService.updateCart(1L, cartItem)).thenReturn(java.util.Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> cartController.updateCart(1L, cartItem));
        assertEquals("Cart not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartService, times(1)).updateCart(1L, cartItem);
    }

    @Test
    public void testRemoveCartItem_Success() {
        when(cartService.getcartById(1L)).thenReturn(java.util.Optional.of(cartItem));

        ResponseEntity<String> response = cartController.removeCartItem(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item removed from cart successfully.", response.getBody());
        verify(cartService, times(1)).removeCartItem(1L);
    }

    @Test
    public void testRemoveCartItem_NotFound() {
        when(cartService.getcartById(1L)).thenReturn(java.util.Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> cartController.removeCartItem(1L));
        assertEquals("Cart not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartService, times(1)).getcartById(1L);
    }
}
