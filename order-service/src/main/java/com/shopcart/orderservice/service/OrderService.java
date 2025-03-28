package com.shopcart.orderservice.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shopcart.orderservice.clients.CartFeignClient;
import com.shopcart.orderservice.dto.CartDto;
import com.shopcart.orderservice.entity.Order;
import com.shopcart.orderservice.entity.Payment;
import com.shopcart.orderservice.exception.CustomException;
import com.shopcart.orderservice.repository.OrderRepository;
import com.shopcart.orderservice.repository.PaymentRepository;

import feign.FeignException;
import lombok.Data;


@Service
@Data
public class OrderService {
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private CartFeignClient cartFeignClient;
    
    public Order createOrder(Long cartId) {
        log.info("Fetching cart details for cartId: {}", cartId);
        
        // Fetch the cart details using Feign client
        CartDto cartDto = cartFeignClient.getCartById(cartId);
        
        log.info("Received cart details: {}", cartDto);
 
        if (cartDto == null) {
            throw new CustomException("Cart not found with ID: " + cartId, HttpStatus.NOT_FOUND);
        }
        
        if (cartDto.getCartValue() == null) {
            throw new CustomException("Cart value cannot be null for cart ID: " + cartId, HttpStatus.BAD_REQUEST);
        }
 
        Order order = new Order();
        order.setCartId(cartId);
        order.setCartValue(cartDto.getCartValue()); // Ensure this is not null
        order.setOrderPaymentStatus("CREATED");
 
        return orderRepository.save(order);
    }
    
    
    
    public Payment processPayment(Long orderId, Payment payment) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new CustomException("Order not found with ID: " + orderId, HttpStatus.NOT_FOUND);
        }
      
        payment.setId(orderId);
        return payment;
    }
 
 
    public List<Order> getAllOrders() {
    	return orderRepository.findAll();
    	}
 
    public Optional<Order> getOrderById(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new CustomException("Order not found with ID: " + orderId, HttpStatus.NOT_FOUND);
        }
        return orderOpt;
    }
 
    public String getOrderStatusById(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            return orderOpt.get().getOrderPaymentStatus();
        } else {
            throw new CustomException("Order not found with ID: " + orderId, HttpStatus.NOT_FOUND);
        }
    }
        
    
    public Order confirmOrderPayment(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderPaymentStatus("CONFIRMED");
            return orderRepository.save(order);
        } else {
            throw new CustomException("Order not found with ID: " + orderId, HttpStatus.NOT_FOUND);
        }
    }
 
 
 
	public static Logger getLog() {
		return log;
	}
    
    
    
 
 
	
   }
    
 