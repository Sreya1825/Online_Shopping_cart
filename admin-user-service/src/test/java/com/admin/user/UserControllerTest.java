package com.admin.user;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.adminuser.controller.UserController;
import com.adminuser.entity.CartItems;
import com.adminuser.feign.CartFeign;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private CartFeign cartFeign;

    private CartItems cartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartItem = new CartItems();
        cartItem.setCartId(1L);
        cartItem.setProductId(1L);  // Assuming productId is a Long

    }

    @Test
    public void testGetAllCartList() {
        when(cartFeign.getAllCartList()).thenReturn(ResponseEntity.ok(Collections.singletonList(cartItem)));

        ResponseEntity<List<CartItems>> response = userController.getAllCartList();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(cartItem, response.getBody().get(0));
        verify(cartFeign, times(1)).getAllCartList();
    }

    @Test
    public void testGetCartById() {
        when(cartFeign.getCartById(1L)).thenReturn(ResponseEntity.ok(cartItem));

        ResponseEntity<CartItems> response = userController.getCartById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItem, response.getBody());
        verify(cartFeign, times(1)).getCartById(1L);
    }

    @Test
    public void testAddToCart() {
        when(cartFeign.addToCart(any(CartItems.class))).thenReturn(ResponseEntity.ok(cartItem));

        ResponseEntity<CartItems> response = userController.addToCart(cartItem);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItem, response.getBody());
        verify(cartFeign, times(1)).addToCart(any(CartItems.class));
    }

    @Test
    public void testUpdateCart() {
        when(cartFeign.updateCart(eq(1L), any(CartItems.class))).thenReturn(ResponseEntity.ok(cartItem));

        ResponseEntity<CartItems> response = userController.updateCart(1L, cartItem);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItem, response.getBody());
        verify(cartFeign, times(1)).updateCart(eq(1L), any(CartItems.class));
    }

    @Test
    public void testRemoveCartItem() {
        when(cartFeign.removeCartItem(1L)).thenReturn(ResponseEntity.ok("Cart item removed"));

        ResponseEntity<String> response = userController.removeCartItem(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cart item removed", response.getBody());
        verify(cartFeign, times(1)).removeCartItem(1L);
    }
}
