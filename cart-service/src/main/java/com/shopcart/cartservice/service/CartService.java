package com.shopcart.cartservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shopcart.cartservice.clients.ProductFeginClient;
import com.shopcart.cartservice.dto.ProductDto;
import com.shopcart.cartservice.entity.CartItems;
import com.shopcart.cartservice.exception.*;
import com.shopcart.cartservice.repository.CartRepository;
import com.shopcart.cartservice.responses.ResponseCartDelete;

import feign.FeignException;



@Service
public class CartService {
	//private static final Logger log = LoggerFactory.getLogger(CartService.class);
	
	@Autowired
	CartRepository cartRespository;
	
	@Autowired
	ProductFeginClient productFeginClient;

	
	public List<CartItems> getAllCartList() {
		return cartRespository.findAll();

	}

	public Optional<CartItems> getcartById(Long id) {
		return Optional.of(cartRespository.findById(id)
			.orElseThrow(() -> new CustomException("Cart not found with ID: " + id, HttpStatus.NOT_FOUND)));
	}

	public Optional<CartItems> updateCart(Long id, CartItems cart) {
	    CartItems existingCart = cartRespository.findById(id)
	            .orElseThrow(() -> new CustomException("Cart not found with ID: " + id, HttpStatus.NOT_FOUND));
	 
	    
	    if ("Not Available".equalsIgnoreCase(cart.getProductStatus())) {
	        throw new CustomException("Product is not available", HttpStatus.BAD_REQUEST);
	    }
	 
	    existingCart.setProductId(cart.getProductId());
	    existingCart.setProductStatus(cart.getProductStatus());
	    existingCart.setQuantity(cart.getQuantity());
	    existingCart.setUserId(cart.getUserId());
	 
	    // **Recalculate cartValue**
	    BigDecimal productPrice = fetchProductPrice(cart.getProductId());
	    BigDecimal updatedCartValue = productPrice.multiply(BigDecimal.valueOf(cart.getQuantity()));
	    existingCart.setCartValue(updatedCartValue);
	 
	    CartItems updatedCart = cartRespository.save(existingCart);
	    return Optional.of(updatedCart);
	}

	public ResponseCartDelete deleteCart(Long id) {
		if (!cartRespository.existsById(id)) {
			throw new CustomException("Cart not found with ID: " + id, HttpStatus.NOT_FOUND);
		}
		cartRespository.deleteById(id);
		ResponseCartDelete deleteResponse = new ResponseCartDelete();
		deleteResponse.setId(id);
		deleteResponse.setDeleteMsg("Cart deleted successfully.");
		return deleteResponse;
	}
	public BigDecimal fetchProductPrice(Long productId) {
	    try {
	        ProductDto product = productFeginClient.getProductById(productId);
	        return product.getPrice();
	    } catch (FeignException e) {
	        if (e.status() == 404) {
	            throw new CustomException("Product with ID " + productId + " not found", HttpStatus.NOT_FOUND);
	        }
	        throw new CustomException("Error fetching product details", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}



	public CartItems addToCart(CartItems cart) {
	    if ("Not Available".equalsIgnoreCase(cart.getProductStatus())) {
	        throw new CustomException("Product is not available", HttpStatus.BAD_REQUEST);
	    }
	 
	    BigDecimal productPrice = fetchProductPrice(cart.getProductId());
	    BigDecimal cartValue = productPrice.multiply(BigDecimal.valueOf(cart.getQuantity()));
	    cart.setCartValue(cartValue);
	    return cartRespository.save(cart);
	}

	public List<CartItems> getCartByUserId(Long userId) {
		List<CartItems> cartItems = cartRespository.findByUserId(userId);
		if (cartItems.isEmpty()) {
			throw new CustomException("No cart items found for user with ID: " + userId, HttpStatus.NOT_FOUND);
		}
		return cartItems;
	}

	public List<CartItems> getCartByProductId(Long productId) {
		List<CartItems> cartItems = cartRespository.findByProductId(productId);
		if (cartItems.isEmpty()) {
			throw new CustomException("No cart items found for product with ID: " + productId, HttpStatus.NOT_FOUND);
		}
		return cartItems;
	}

	public void removeCartItem(Long cartId) {
		if (!cartRespository.existsById(cartId)) {
			throw new CustomException("Cart item not found with ID: " + cartId, HttpStatus.NOT_FOUND);
		}
		cartRespository.deleteById(cartId);
	}

}
