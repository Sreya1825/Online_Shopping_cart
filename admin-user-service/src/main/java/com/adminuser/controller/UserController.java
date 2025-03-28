
package com.adminuser.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.adminuser.entity.CartItems;
import com.adminuser.feign.CartFeign;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins="*")
public class UserController {
	@Autowired
	private CartFeign cf;

	@GetMapping("/cart/getAllCartList")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<CartItems>> getAllCartList() {
		return cf.getAllCartList();
	}

	@GetMapping("/cart/CartById/{id}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<CartItems> getCartById(@PathVariable Long id) {
		return cf.getCartById(id);
	}

//	@GetMapping("/cart/user/{userId}")
//	@PreAuthorize("hasAuthority('USER')")
//	public ResponseEntity<List<CartItems>> getCartByUserId(@PathVariable Long userId) {
//		return cf.getCartByUserId(userId);
//	}
//
//	@GetMapping("/product/{productId}")
//	@PreAuthorize("hasAuthority('USER')")
//	public ResponseEntity<List<CartItems>> getCartByProductId(@PathVariable Long productId) {
//		return cf.getCartByProductId(productId);
//	}

	@PostMapping("/cart/add")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<CartItems> addToCart(@RequestBody @Valid CartItems cart) {
		return cf.addToCart(cart);
	}

	@PutMapping("/cart/update/{id}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<CartItems> updateCart(@PathVariable Long id, @Valid @RequestBody CartItems cart) {
		return cf.updateCart(id, cart);
	}

	@DeleteMapping("/cart/remove/{cartId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<String> removeCartItem(@PathVariable Long cartId) {
		return cf.removeCartItem(cartId);
	}
}
