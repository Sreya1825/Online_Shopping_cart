package com.shopcart.categorytest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import com.shopcart.categoryservice.clients.ProductFeginClient;
import com.shopcart.categoryservice.dto.CategoryDto;
import com.shopcart.categoryservice.dto.ProductDto;
import com.shopcart.categoryservice.entity.Category;
import com.shopcart.categoryservice.exception.CustomException;
import com.shopcart.categoryservice.repository.CategoryRepository;
import com.shopcart.categoryservice.service.CategoryService;

public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private ProductFeginClient productFeginClient;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setSlug("electronics");
        category.setProductId(Collections.singletonList(1L));

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Electronics");
        categoryDto.setSlug("electronics");
        categoryDto.setProductDto(Collections.singletonList(new ProductDto()));
        categoryDto.setStatus("Successfully fetched products..");
        categoryDto.setProductMessage("Products are available for this category");
    }

    @Test
    public void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        List<Category> categories = categoryService.getAllCategories(null);
        assertEquals(1, categories.size());
        assertEquals(category, categories.get(0));
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productFeginClient.getProductById(1L)).thenReturn(new ProductDto());

        Optional<CategoryDto> result = categoryService.getCategoryById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(categoryDto.getId(), result.get().getId());
        assertEquals(categoryDto.getName(), result.get().getName());
        
        verify(categoryRepository, times(1)).findById(1L);
    }


    @Test
    public void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.getCategoryById(1L));
        assertEquals("Category not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);
        assertEquals(category, createdCategory);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testCreateCategory_Null() {
        CustomException exception = assertThrows(CustomException.class, () -> categoryService.createCategory(null));
        assertEquals("Invalid category data", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void testUpdateCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Optional<Category> result = categoryService.updateCategory(1L, category);
        assertTrue(result.isPresent());
        assertEquals(category, result.get());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.updateCategory(1L, category));
        assertEquals("Category not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(1L);

        boolean result = categoryService.deleteCategory(1L);
        assertTrue(result);
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.deleteCategory(1L));
        assertEquals("Category not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testProductFallBackMethod_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<CategoryDto> result = categoryService.productFallBackMethod(1L, new Exception("Service Down"));
        assertTrue(result.isPresent());
        assertEquals(categoryDto.getName(), result.get().getName());
        assertEquals("Product Service is unavailable or down, please try again later..", result.get().getStatus());
    }

    @Test
    public void testProductFallBackMethod_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.productFallBackMethod(1L, new Exception("Service Down")));
        assertEquals("Category not found during fallback with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}

