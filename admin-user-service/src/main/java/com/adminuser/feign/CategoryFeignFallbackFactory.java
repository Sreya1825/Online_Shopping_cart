package com.adminuser.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.adminuser.entity.Category;
import com.adminuser.exception.ResourceNotFoundException;

import java.util.List;

@Component
public class CategoryFeignFallbackFactory implements FallbackFactory<CategoryFeign> {

    @Override
    public CategoryFeign create(Throwable cause) {
        return new CategoryFeign() {

            @Override
            public ResponseEntity<List<Category>> getAllCategories() {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(null);
            }

            @Override
            public ResponseEntity<?> getCategoryById(Long id) {
                if (isNotFound(cause)) {
                    throw new ResourceNotFoundException("Category with ID " + id + " not found");
                }
                throw new RuntimeException("Service unavailable while fetching category");
            }

            @Override
            public ResponseEntity<Category> createCategory(Category category) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(null);
            }

            @Override
            public ResponseEntity<?> updateCategory(Long id, Category categoryUpdate) {
                if (isNotFound(cause)) {
                    throw new ResourceNotFoundException("Category with ID " + id + " not found for update");
                }
                throw new RuntimeException("Service unavailable while updating category");
            }

            @Override
            public ResponseEntity<?> deleteCategory(Long id) {
                if (isNotFound(cause)) {
                    throw new ResourceNotFoundException("Category with ID " + id + " not found for deletion");
                }
                throw new RuntimeException("Service unavailable while deleting category");
            }

            // Utility method to check if the error is a 404 (Not Found)
            private boolean isNotFound(Throwable cause) {
                return cause instanceof FeignException && ((FeignException) cause).status() == 404;
            }
        };
    }
}
