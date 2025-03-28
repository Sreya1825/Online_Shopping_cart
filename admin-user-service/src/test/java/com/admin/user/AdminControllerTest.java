package com.admin.user;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.adminuser.controller.AdminController;
import com.adminuser.entity.Product;
import com.adminuser.feign.ProductFeign;

public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private ProductFeign productFeign;

    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
    }

    @Test
    public void testGetAllProducts() {
        when(productFeign.getAllProducts()).thenReturn(ResponseEntity.ok(Collections.singletonList(product)));

        ResponseEntity<List<Product>> response = adminController.getAllProducts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(product, response.getBody().get(0));
        verify(productFeign, times(1)).getAllProducts();
    }

    @Test
    public void testGetProductById() {
        doReturn(ResponseEntity.ok(product)).when(productFeign).getProductById(1L); // ✅ Use doReturn()

        ResponseEntity<?> response = adminController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productFeign, times(1)).getProductById(1L);
    }



    @Test
    public void testCreateProduct() {
        when(productFeign.createProduct(any(Product.class))).thenReturn(ResponseEntity.ok(product));

        ResponseEntity<Product> response = adminController.createProduct(product);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productFeign, times(1)).createProduct(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {
        ResponseEntity<?> responseEntity = ResponseEntity.ok(product);

        doReturn(responseEntity).when(productFeign).updateProduct(eq(1L), any(Product.class)); // Fix type capture

        ResponseEntity<?> response = adminController.updateProduct(1L, product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productFeign, times(1)).updateProduct(eq(1L), any(Product.class));
    }




    @Test
    public void testDeleteProduct() {
        when(productFeign.deleteProduct(1L)).thenReturn(ResponseEntity.ok(true));

        ResponseEntity<Boolean> response = adminController.deleteProduct(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(productFeign, times(1)).deleteProduct(1L);
    }
}