package com.shopcart.ordertest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.shopcart.orderservice.clients.CartFeignClient;
import com.shopcart.orderservice.dto.CartDto;
import com.shopcart.orderservice.entity.Order;
import com.shopcart.orderservice.entity.Payment;
import com.shopcart.orderservice.exception.CustomException;
import com.shopcart.orderservice.repository.OrderRepository;
import com.shopcart.orderservice.repository.PaymentRepository;
import com.shopcart.orderservice.service.OrderService;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CartFeignClient cartFeignClient;

    private Order order;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setOrderId(1L);
        order.setCartId(1L);
        order.setOrderPaymentStatus("CREATED");
    }

   
    @Test
    public void testCreateOrder() {
        // Mock the CartDto response from Feign client
        CartDto cartDto = new CartDto();
        cartDto.setCartId(1L);
        cartDto.setCartValue(new BigDecimal("500.00")); // Use BigDecimal

        when(cartFeignClient.getCartById(1L)).thenReturn(cartDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(1L);

        assertNotNull(createdOrder);
        assertEquals("CREATED", createdOrder.getOrderPaymentStatus());

        // Verify that the Feign client and repository were called
        verify(cartFeignClient, times(1)).getCartById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }


    @Test
    public void testProcessPayment_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Payment payment = new Payment();
        payment.setId(1L);
        
        Payment processedPayment = orderService.processPayment(1L, payment);
        assertEquals(1L, processedPayment.getId());
        verify(paymentRepository, times(0)).save(any(Payment.class)); // No save call expected
    }

    @Test
    public void testProcessPayment_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> orderService.processPayment(1L, new Payment()));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.getAllOrders();
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> retrievedOrder = orderService.getOrderById(1L);
        assertTrue(retrievedOrder.isPresent());
        assertEquals(order, retrievedOrder.get());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> orderService.getOrderById(1L));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetOrderStatusById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String orderStatus = orderService.getOrderStatusById(1L);
        assertEquals("CREATED", orderStatus);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetOrderStatusById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> orderService.getOrderStatusById(1L));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testConfirmOrderPayment_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order confirmedOrder = orderService.confirmOrderPayment(1L);
        assertEquals("CONFIRMED", confirmedOrder.getOrderPaymentStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testConfirmOrderPayment_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> orderService.confirmOrderPayment(1L));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
