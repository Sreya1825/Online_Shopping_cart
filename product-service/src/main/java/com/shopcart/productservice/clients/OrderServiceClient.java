package com.shopcart.productservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ORDERSERVICE", fallback = OrderServiceFallBack.class)
public interface OrderServiceClient {
    @GetMapping("/orders/status/{orderId}")
    String getOrderStatus(@PathVariable Long orderId);
}
