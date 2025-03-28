package com.shopcart.carttest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import com.shopcart.cartservice.clients.ProductFeginClient;
import com.shopcart.cartservice.dto.ProductDto;
import com.shopcart.cartservice.entity.CartItems;
import com.shopcart.cartservice.exception.CustomException;
import com.shopcart.cartservice.repository.CartRepository;
import com.shopcart.cartservice.responses.ResponseCartDelete;
import com.shopcart.cartservice.service.CartService;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRespository;

    @Mock
    private ProductFeginClient productFeginClient;

    private CartItems cartItem;
    private ProductDto productDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartItem = new CartItems();
        cartItem.setCartId(1L);
        cartItem.setProductId(1L);
        cartItem.setQuantity(2);
        cartItem.setUserId(1L);
        cartItem.setProductStatus("Available");

        productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setPrice(new BigDecimal("100.00"));
    }

    @Test
    public void testGetAllCartList() {
        when(cartRespository.findAll()).thenReturn(Collections.singletonList(cartItem));

        List<CartItems> cartItems = cartService.getAllCartList();
        assertEquals(1, cartItems.size());
        assertEquals(cartItem, cartItems.get(0));
        verify(cartRespository, times(1)).findAll();
    }

    @Test
    public void testGetCartById_Success() {
        when(cartRespository.findById(1L)).thenReturn(Optional.of(cartItem));

        Optional<CartItems> result = cartService.getcartById(1L);
        assertTrue(result.isPresent());
        assertEquals(cartItem, result.get());
        verify(cartRespository, times(1)).findById(1L);
    }

    @Test
    public void testGetCartById_NotFound() {
        when(cartRespository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> cartService.getcartById(1L));
        assertEquals("Cart not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartRespository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateCart_Success() {
        // Create a mock CartItems
        CartItems cartItem = new CartItems();
        cartItem.setCartId(1L);
        cartItem.setQuantity(2);
        cartItem.setProductId(100L);
        cartItem.setUserId(200L);
        cartItem.setProductStatus("Available");

        // Mock ProductDto
        ProductDto mockProduct = new ProductDto();
        mockProduct.setPrice(new BigDecimal("250.00")); // Ensure price is set

        // Mock repository and Feign client behavior
        when(cartRespository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(productFeginClient.getProductById(100L)).thenReturn(mockProduct); // Mock Feign response
        when(cartRespository.save(any(CartItems.class))).thenReturn(cartItem);

        // Call updateCart
        Optional<CartItems> result = cartService.updateCart(1L, cartItem);

        // Verify result
        assertTrue(result.isPresent());
        assertEquals(cartItem.getProductId(), result.get().getProductId());
        assertEquals(cartItem.getQuantity(), result.get().getQuantity());

        // Verify interactions
        verify(cartRespository, times(1)).findById(1L);
        verify(productFeginClient, times(1)).getProductById(100L);
        verify(cartRespository, times(1)).save(any(CartItems.class));
    }



    @Test
    public void testUpdateCart_NotFound() {
        when(cartRespository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> cartService.updateCart(1L, cartItem));
        assertEquals("Cart not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartRespository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteCart_Success() {
        when(cartRespository.existsById(1L)).thenReturn(true);
        doNothing().when(cartRespository).deleteById(1L);

        ResponseCartDelete response = cartService.deleteCart(1L);
        assertEquals(1L, response.getId());
        assertEquals("Cart deleted successfully.", response.getDeleteMsg());
        verify(cartRespository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCart_NotFound() {
        when(cartRespository.existsById(1L)).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> cartService.deleteCart(1L));
        assertEquals("Cart not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartRespository, times(1)).existsById(1L);
    }

    @Test
    public void testFetchProductPrice_Success() {
        when(productFeginClient.getProductById(1L)).thenReturn(productDto);

        BigDecimal price = cartService.fetchProductPrice(1L);
        assertEquals(new BigDecimal("100.00"), price);
        verify(productFeginClient, times(1)).getProductById(1L);
    }

    public BigDecimal fetchProductPrice(Long productId) {
        ProductDto product = productFeginClient.getProductById(productId);
        if (product == null) {
            throw new CustomException("Product not found with ID: " + productId, HttpStatus.NOT_FOUND);
        }
        return product.getPrice();
    }


    @Test
    public void testAddToCart() {
        when(productFeginClient.getProductById(1L)).thenReturn(productDto);
        when(cartRespository.save(any(CartItems.class))).thenReturn(cartItem);

        CartItems result = cartService.addToCart(cartItem);
        assertEquals(cartItem, result);
        verify(cartRespository, times(1)).save(any(CartItems.class));
    }

    @Test
    public void testGetCartByUserId_Success() {
        when(cartRespository.findByUserId(1L)).thenReturn(Collections.singletonList(cartItem));

        List<CartItems> result = cartService.getCartByUserId(1L);
        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
        verify(cartRespository, times(1)).findByUserId(1L);
    }

    @Test
    public void testGetCartByUserId_NotFound() {
        when(cartRespository.findByUserId(1L)).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> cartService.getCartByUserId(1L));
        assertEquals("No cart items found for user with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartRespository, times(1)).findByUserId(1L);
    }

    @Test
    public void testGetCartByProductId_Success() {
        when(cartRespository.findByProductId(1L)).thenReturn(Collections.singletonList(cartItem));

        List<CartItems> result = cartService.getCartByProductId(1L);
        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
        verify(cartRespository, times(1)).findByProductId(1L);
    }

    @Test
    public void testGetCartByProductId_NotFound() {
        when(cartRespository.findByProductId(1L)).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> cartService.getCartByProductId(1L));
        assertEquals("No cart items found for product with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartRespository, times(1)).findByProductId(1L);
    }

    @Test
    public void testRemoveCartItem_Success() {
        when(cartRespository.existsById(1L)).thenReturn(true);
        doNothing().when(cartRespository).deleteById(1L);

        cartService.removeCartItem(1L);
        verify(cartRespository, times(1)).deleteById(1L);
    }

    @Test
    public void testRemoveCartItem_NotFound() {
        when(cartRespository.existsById(1L)).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> cartService.removeCartItem(1L));
        assertEquals("Cart item not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(cartRespository, times(1)).existsById(1L);
    }
}
