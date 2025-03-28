package com.shopcart.productservice.clients;

import org.springframework.stereotype.Component;

@Component
public class OrderServiceFallBack implements OrderServiceClient {
    @Override
    public String getOrderStatus(Long orderId) {
        return "Order Status Unavailable";
    }
}
