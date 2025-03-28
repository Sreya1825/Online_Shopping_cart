 package com.shopcart.categorytest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.shopcart.categoryservice.controller.CategoryController;
import com.shopcart.categoryservice.dto.CategoryDto;
import com.shopcart.categoryservice.entity.Category;
import com.shopcart.categoryservice.error.ErrorResponse;
import com.shopcart.categoryservice.exception.CustomException;
import com.shopcart.categoryservice.service.CategoryService;

public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Electronics");
    }

    @Test
    public void testGetAllCategories() {
        when(categoryService.getAllCategories(null)).thenReturn(Collections.singletonList(category));

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(category, response.getBody().get(0));
        verify(categoryService, times(1)).getAllCategories(null);
    }

    @Test
    public void testGetCategoryById_Success() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(categoryDto));

        ResponseEntity<?> response = categoryController.getCategoryById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    public void testGetCategoryById_NotFound() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = categoryController.getCategoryById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Category not found with ID: 1", errorResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatus());
        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    public void testCreateCategory() {
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        ResponseEntity<Category> response = categoryController.createCategory(category);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    public void testUpdateCategory_Success() {
        when(categoryService.updateCategory(1L, category)).thenReturn(Optional.of(category));

        ResponseEntity<?> response = categoryController.updateCategory(1L, category);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).updateCategory(1L, category);
    }

    @Test
    public void testUpdateCategory_NotFound() {
        when(categoryService.updateCategory(1L, category)).thenReturn(Optional.empty());

        ResponseEntity<?> response = categoryController.updateCategory(1L, category);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Category not found with ID: 1", errorResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatus());
        verify(categoryService, times(1)).updateCategory(1L, category);
    }

    @Test
    public void testDeleteCategory_Success() {
        // No need for doNothing(), just call the method and check behavior
        ResponseEntity<ErrorResponse> response = categoryController.deleteCategory(1L);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());  
        assertEquals("Deleted category successfully", response.getBody().getMessage());  
        verify(categoryService, times(1)).deleteCategory(1L);
    }


    @Test
    public void testDeleteCategory_NotFound() {
        doThrow(new CustomException("Category not found with ID: 1", HttpStatus.NOT_FOUND)).when(categoryService).deleteCategory(1L);

        ResponseEntity<?> response = categoryController.deleteCategory(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Category not found with ID: 1", errorResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatus());
        verify(categoryService, times(1)).deleteCategory(1L);
    }
}
