package com.shopcart.ordertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.shopcart.orderservice.controller.OrderController;
import com.shopcart.orderservice.entity.Order;
import com.shopcart.orderservice.entity.Payment;
import com.shopcart.orderservice.errorhandler.ErrorMessage;
import com.shopcart.orderservice.exception.CustomException;
import com.shopcart.orderservice.service.OrderService;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private Order order;
    private Payment payment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setOrderId(1L);
        order.setCartId(1L);
        order.setOrderPaymentStatus("CREATED");

        payment = new Payment();
        payment.setId(1L);
        payment.setAmount((float) 100.00);
    }

    @Test
    public void testCreateOrder() {
        when(orderService.createOrder(1L)).thenReturn(order);

        ResponseEntity<Order> response = orderController.createOrder(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).createOrder(1L);
    }

    @Test
    public void testProcessPayment_Success() {
        when(orderService.processPayment(1L, payment)).thenReturn(payment);

        ResponseEntity<Payment> response = orderController.processPayment(1L, payment);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payment, response.getBody());
        verify(orderService, times(1)).processPayment(1L, payment);
    }

    @Test
    public void testConfirmOrderPayment_Success() {
        when(orderService.confirmOrderPayment(1L)).thenReturn(order);

        ResponseEntity<Order> response = orderController.confirmOrderPayment(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).confirmOrderPayment(1L);
    }

    @Test
    public void testGetAllOrders() {
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(order));

        ResponseEntity<List<Order>> response = orderController.getAllOrders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(order, response.getBody().get(0));
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void testGetOrderById_Success() {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        ResponseEntity<?> response = orderController.getOrderById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    public void testGetOrderById_NotFound() {
        when(orderService.getOrderById(1L)).thenThrow(new CustomException("Not found", HttpStatus.NOT_FOUND));

        ResponseEntity<?> response = orderController.getOrderById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorMessage errorMessage = (ErrorMessage) response.getBody();
        assertEquals("Not found", errorMessage.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
    }

    @Test
    public void testGetOrderStatus_Success() {
        when(orderService.getOrderStatusById(1L)).thenReturn("CREATED");

        ResponseEntity<String> response = orderController.getOrderStatus(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CREATED", response.getBody());
        verify(orderService, times(1)).getOrderStatusById(1L);
    }
}
