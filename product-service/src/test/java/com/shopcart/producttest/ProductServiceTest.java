package com.shopcart.producttest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

import com.shopcart.productservice.clients.OrderServiceClient;
import com.shopcart.productservice.dto.ProductDto;
import com.shopcart.productservice.entity.Product;
import com.shopcart.productservice.exception.CustomException;
import com.shopcart.productservice.repository.ProductRepository;
import com.shopcart.productservice.service.ProductService;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderServiceClient orderServiceClient;

    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setProductType("Electronics");
        product.setCategory("Gadgets");
        product.setPrice(BigDecimal.valueOf(1000.00));

        product.setDescription("Test product description");
        product.setImage("test-image.jpg");
        product.setRating(4);
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getAllProducts();
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testCreateProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);
        assertEquals(product, createdProduct);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testCreateProduct_Failure() {
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> productService.createProduct(product));
        assertEquals("Error creating product: Database error", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    @Test
    public void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getProductById(1L);
        assertTrue(foundProduct.isPresent());
        assertEquals(product, foundProduct.get());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productService.getProductById(1L));
        assertEquals("Product with ID 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateProduct_Success() {
        Product updateProduct = new Product();
        updateProduct.setProductName("Updated Product");
        updateProduct.setProductType("Updated Type");
        updateProduct.setCategory("Updated Category");
        updateProduct.setPrice(BigDecimal.valueOf(1200.00));

        updateProduct.setDescription("Updated description");
        updateProduct.setImage("updated-image.jpg");
        updateProduct.setRating(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updateProduct);

        Optional<Product> updatedProduct = productService.updateProduct(1L, updateProduct);
        assertTrue(updatedProduct.isPresent());
        assertEquals("Updated Product", updatedProduct.get().getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_NotFound() {
        Product updateProduct = new Product();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productService.updateProduct(1L, updateProduct));
        assertEquals("Product with ID 1 not found for update", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

//    @Test
//    public void testDeleteProduct_Success() {
//        when(productRepository.existsById(1L)).thenReturn(true);
//
//        assertTrue(productService.deleteProduct(1L));
//        verify(productRepository, times(1)).deleteById(1L);
//    }
    @Test
    public void testDeleteProduct_Success() {
        Long productId = 1L;

        // Mocking repository behavior
        when(productRepository.existsById(productId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(productId);

        // Call the service method
        String result = productService.deleteProduct(productId);

        // Assertions
        assertEquals("Product with ID " + productId + " deleted successfully", result);
        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> productService.deleteProduct(1L));
        assertEquals("Product with ID 1 not found for deletion", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetProductByIdWithOrder_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderServiceClient.getOrderStatus(anyLong())).thenReturn("Order status");

        Optional<ProductDto> productDto = productService.getProductById(1L, 1L);
        assertTrue(productDto.isPresent());
        assertEquals("Order status", productDto.get().getOrderStatus());
    }

    @Test
    public void testGetProductByIdWithOrder_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productService.getProductById(1L, 1L));
        assertEquals("Product with ID 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testOrderBreakerFallbackMethod_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<ProductDto> productDto = productService.orderBreakerfallBackMethod(1L, 1L, new RuntimeException("Test"));
        assertTrue(productDto.isPresent());
        assertEquals("Order status is unavailable", productDto.get().getOrderStatus());
    }

    @Test
    public void testOrderBreakerFallbackMethod_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productService.orderBreakerfallBackMethod(1L, 1L, new RuntimeException("Test")));
        assertEquals("Product with ID 1 not found during fallback", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetProductByType_Success() {
        when(productRepository.findByProductType("Electronics")).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getProductByType("Electronics");
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
        verify(productRepository, times(1)).findByProductType("Electronics");
    }

    @Test
    public void testGetProductByType_NotFound() {
        when(productRepository.findByProductType("NonExisting")).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> productService.getProductByType("NonExisting"));
        assertEquals("No products found for type: NonExisting", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    
}
