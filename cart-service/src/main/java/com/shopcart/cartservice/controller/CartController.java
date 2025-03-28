package com.shopcart.cartservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopcart.cartservice.entity.Cart;
import com.shopcart.cartservice.entity.CartItems;
import com.shopcart.cartservice.exception.CustomException;
import com.shopcart.cartservice.responses.ErrorResponse;
import com.shopcart.cartservice.responses.ResponseCartDelete;
import com.shopcart.cartservice.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins="*")
public class CartController {
	private static final Logger log = LoggerFactory.getLogger(CartService.class);

	@Autowired
	CartService cartService;
	
	@Autowired
	Environment environment;

	public CartController(CartService cartService) {

		this.cartService = cartService;
		
	}
    @GetMapping("/getAllCartList")
    public ResponseEntity<List<CartItems>> getAllCartList() {
        List<CartItems> allCartList = cartService.getAllCartList();
        return new ResponseEntity<>(allCartList, HttpStatus.OK);
    }

    @GetMapping("/CartById/{id}")
    public ResponseEntity<CartItems> getCartById(@PathVariable Long id) {
        CartItems cart = cartService.getcartById(id).orElseThrow(() ->
            new CustomException("Cart not found with ID: " + id, HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItems>> getCartByUserId(@PathVariable Long userId) {
        List<CartItems> cartItems = cartService.getCartByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new CustomException("No carts found for user ID: " + userId, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cartItems);
    }
	 
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CartItems>> getCartByProductId(@PathVariable Long productId) {
        List<CartItems> cartItems = cartService.getCartByProductId(productId);
        if (cartItems.isEmpty()) {
            throw new CustomException("No carts found for product ID: " + productId, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cartItems);
    }
	 
    @PostMapping("/add")
    public ResponseEntity<CartItems> addToCart(@RequestBody @Valid CartItems cart) {
        CartItems savedCart = cartService.addToCart(cart);
        return ResponseEntity.ok(savedCart);
    }

	
    @PutMapping("/update/{id}")
    public ResponseEntity<CartItems> updateCart(@PathVariable Long id, @Valid @RequestBody CartItems cart) {
        CartItems updatedCart = cartService.updateCart(id, cart).orElseThrow(() ->
            new CustomException("Cart not found with ID: " + id, HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

	
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long cartId) {
        cartService.getcartById(cartId).orElseThrow(() ->
            new CustomException("Cart not found with ID: " + cartId, HttpStatus.NOT_FOUND));
        cartService.removeCartItem(cartId);
        return ResponseEntity.ok("Item removed from cart successfully.");
    }

}
