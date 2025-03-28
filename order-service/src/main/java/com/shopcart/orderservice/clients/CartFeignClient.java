package com.shopcart.orderservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopcart.orderservice.dto.CartDto;

@FeignClient(name = "CARTSERVICE")
public interface CartFeignClient {
    @GetMapping("/cart/CartById/{id}")
    CartDto getCartById(@PathVariable("id") Long id);
}
 