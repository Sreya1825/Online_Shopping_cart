package com.adminuser.feign;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.adminuser.entity.CartItems;
import com.adminuser.exception.ResourceNotFoundException;

import java.util.List;

@Component
public class CartFeignFallbackFactory implements FallbackFactory<CartFeign> {

    @Override
    public CartFeign create(Throwable cause) {
        return new CartFeign() {

            @Override
            public ResponseEntity<List<CartItems>> getAllCartList() {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
            }

            @Override
            public ResponseEntity<CartItems> getCartById(Long id) {
                if (isNotFound(cause)) {
                    throw new ResourceNotFoundException("Cart item with ID " + id + " not found");
                }
                throw new RuntimeException("Service unavailable while fetching cart item");
            }

            @Override
            public ResponseEntity<CartItems> addToCart(CartItems cart) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
            }

            @Override
            public ResponseEntity<CartItems> updateCart(Long id, CartItems cart) {
                if (isNotFound(cause)) {
                    throw new ResourceNotFoundException("Cart item with ID " + id + " not found for update");
                }
                throw new RuntimeException("Service unavailable while updating cart item");
            }

            @Override
            public ResponseEntity<String> removeCartItem(Long cartId) {
                if (isNotFound(cause)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Cart item with ID " + cartId + " not found for deletion");
                }
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service unavailable while deleting cart item");
            }

            // Utility method to check if the error is a 404 (Not Found)
            private boolean isNotFound(Throwable cause) {
                return cause instanceof FeignException && ((FeignException) cause).status() == 404;
            }
        };
    }
}

