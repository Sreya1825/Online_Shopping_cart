package com.adminuser.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.adminuser.entity.CartItems;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@FeignClient("CARTSERVICE")
public interface CartFeign {
	
	@GetMapping("/cart/getAllCartList")
	    public ResponseEntity<List<CartItems>> getAllCartList();

 
	@GetMapping("/cart/CartById/{id}")
	    public ResponseEntity<CartItems> getCartById(@PathVariable Long id);
 
//	    @GetMapping("/cart/user/{userId}")
//	    public ResponseEntity<List<CartItems>> getCartByUserId(@PathVariable Long userId) ;
//	    
//	    @GetMapping("/product/{productId}")
//	    public ResponseEntity<List<CartItems>> getCartByProductId(@PathVariable Long productId);
//	    
	    
	    @PostMapping("/cart/add")
	    public ResponseEntity<CartItems> addToCart(@RequestBody @Valid CartItems cart) ;
 
	    @PutMapping("/cart/update/{id}")
	    public ResponseEntity<CartItems> updateCart(@PathVariable Long id, @Valid @RequestBody CartItems cart) ;
	    
	    
	    @DeleteMapping("/cart/remove/{cartId}")
	    public ResponseEntity<String> removeCartItem(@PathVariable Long cartId);
}
