package com.adminuser.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.adminuser.entity.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Component
public class ProductFeignFallbackFactory implements FallbackFactory<ProductFeign> {

    @Override
    public ProductFeign create(Throwable cause) {
        return new ProductFeign() {

            @Override
            public ResponseEntity<List<Product>> getAllProducts() {
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }

            @Override
            public ResponseEntity<?> getProductById(Long id) {
                if (isNotFound(cause)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(extractErrorMessage(cause, "Product with ID " + id + " not found"));
                }
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service unavailable while fetching product");
            }

            @Override
            public ResponseEntity<Product> createProduct(Product product) {
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }

            @Override
            public ResponseEntity<?> updateProduct(Long id, Product updateProduct) {
                if (isNotFound(cause)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(extractErrorMessage(cause, "Product with ID " + id + " not found for update"));
                }
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service unavailable while updating product");
            }

            @Override
            public ResponseEntity<Boolean> deleteProduct(Long id) {
                if (isNotFound(cause)) {
                    // Returning `false` instead of a String message to match `ResponseEntity<Boolean>`
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
                }
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(false);
            }



            // Utility method to check if the error is a 404 (Not Found)
            private boolean isNotFound(Throwable cause) {
                return cause instanceof FeignException && ((FeignException) cause).status() == 404;
            }

            // Extract only the error message from FeignException response
            private String extractErrorMessage(Throwable cause, String defaultMsg) {
                try {
                    if (cause instanceof FeignException) {
                        String responseBody = ((FeignException) cause).contentUTF8();
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(responseBody);
                        return jsonNode.has("errorMessage") ? jsonNode.get("errorMessage").asText() : defaultMsg;
                    }
                } catch (IOException e) {
                    return defaultMsg;
                }
                return defaultMsg;
            }
        };
    }
}
