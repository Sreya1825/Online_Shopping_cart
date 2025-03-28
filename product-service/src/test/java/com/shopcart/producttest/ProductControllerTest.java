package com.shopcart.producttest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import com.shopcart.productservice.controller.ProductController;
import com.shopcart.productservice.dto.ProductDto;
import com.shopcart.productservice.entity.Product;
import com.shopcart.productservice.exception.CustomException;
import com.shopcart.productservice.service.ProductService;

public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private Product product;

 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setProductId(1L);  // Changed from Long to int
        product.setProductName("Test Product");
        product.setProductType("Electronics");
        product.setCategory("Gadgets");
        product.setPrice(BigDecimal.valueOf(1000.00));

        product.setDescription("Test product description");
        product.setImage("test-image.jpg");
        product.setRating(5);  // Changed from 4.5 (double) to 5 (int)
    }


    @Test
    public void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(product));

        ResponseEntity<List<Product>> response = productController.getAllProducts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testGetProductById_Success() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<?> response = productController.getProductById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productController.getProductById(1L));
        assertEquals("Product not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetProductOrderById_Success() {
        ProductDto productDto = new ProductDto();
        productDto.setProduct(product);
        productDto.setOrderStatus("Confirmed");

        when(productService.getProductById(1L, 1L)).thenReturn(Optional.of(productDto));

        ResponseEntity<?> response = productController.getProductOrderById(1L, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(productService, times(1)).getProductById(1L, 1L);
    }

    @Test
    public void testGetProductOrderById_NotFound() {
        when(productService.getProductById(1L, 1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productController.getProductOrderById(1L, 1L));
        assertEquals("Product not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testCreateProduct_Success() {
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(product);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).createProduct(product);
    }

    @Test
    public void testCreateProduct_Failure() {
        when(productService.createProduct(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> productController.createProduct(product));
        assertEquals("Error creating product: Database error", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    @Test
    public void testUpdateProduct_Success() {
        when(productService.updateProduct(1L, product)).thenReturn(Optional.of(product));

        ResponseEntity<?> response = productController.updateProduct(1L, product);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).updateProduct(1L, product);
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productService.updateProduct(1L, product)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productController.updateProduct(1L, product));
        assertEquals("Product not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testDeleteProduct_Success() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<Boolean> response = productController.deleteProduct(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productController.deleteProduct(1L));
        assertEquals("Product not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetProductByType_Success() {
        when(productService.getProductByType("Electronics")).thenReturn(Collections.singletonList(product));

        ResponseEntity<List<Product>> response = productController.getProductByType("Electronics");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(productService, times(1)).getProductByType("Electronics");
    }

    @Test
    public void testGetProductByType_NotFound() {
        when(productService.getProductByType("Electronics")).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> productController.getProductByType("Electronics"));
        assertEquals("No products found for type: Electronics", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    
}
