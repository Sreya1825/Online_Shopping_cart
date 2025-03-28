package com.shopcart.orderservice.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopcart.orderservice.entity.Order;
import com.shopcart.orderservice.entity.Payment;
import com.shopcart.orderservice.errorhandler.ErrorMessage;
import com.shopcart.orderservice.exception.CustomException;
import com.shopcart.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins="*")
public class OrderController {
 
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	Environment environment;
	
	@Autowired
	OrderService orderService;
    @PostMapping("/create/{cartId}")
    public ResponseEntity<Order> createOrder(@PathVariable Long cartId) {
        Order order = orderService.createOrder(cartId);
        return ResponseEntity.ok(order);
    }
 
    @PostMapping("/payment/{orderId}")
    public ResponseEntity<Payment> processPayment(@PathVariable Long orderId, @RequestBody Payment payment) {
        Payment processedPayment = orderService.processPayment(orderId, payment);
        return ResponseEntity.ok(processedPayment);
    }
 
    @PutMapping("/confirm/{orderId}")
    public ResponseEntity<Order> confirmOrderPayment(@PathVariable Long orderId) {
        Order order = orderService.confirmOrderPayment(orderId);
        return ResponseEntity.ok(order);
    }
 
    @GetMapping("/port")
    public String portNumberStatus() {
        return "OrderService running port number is: " + environment.getProperty("local.server.port");
    }
 
    @GetMapping("/list")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }
 
    @GetMapping("/id/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        try {
            Optional<Order> orderOpt = orderService.getOrderById(orderId);
            return new ResponseEntity<>(orderOpt.get(), HttpStatus.OK);
        } catch (CustomException e) {
            logger.error("Error: {} | Status: {}", e.getMessage(), e.getStatus());
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), e.getStatus()), e.getStatus());
        }
    }
 
	@GetMapping("/status/{orderId}")
	public ResponseEntity<String> getOrderStatus(@PathVariable Long orderId) {
		String orderStatus = orderService.getOrderStatusById(orderId);
		return new ResponseEntity<>(orderStatus, HttpStatus.OK);
	}
 
}