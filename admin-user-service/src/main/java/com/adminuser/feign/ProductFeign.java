package com.adminuser.feign;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import com.oam.entity.Product;
//
//
//import jakarta.validation.Valid;
//
//
//
//@FeignClient("PRODUCTSERVICE", fallbackFactory = ProductFeignFallbackFactory.class)
//public interface ProductFeign {
//    
//	@GetMapping("/product/all")
//	public ResponseEntity<List<Product>> getAllProducts() ;
//	
//	
//	@GetMapping("/product/{id}")
//    public ResponseEntity<?> getProductById(@PathVariable Long id);
//	
//	@PostMapping("/product/create")
//	  public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product);
//	
//	
//	@PutMapping("/product/{id}")
//    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updateProduct);
//	
// 
//    @DeleteMapping("/product/{id}")
//    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id);
//}


import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.adminuser.entity.Product;

import jakarta.validation.Valid;

import java.util.List;

@FeignClient(name = "PRODUCTSERVICE", fallbackFactory = ProductFeignFallbackFactory.class)
public interface ProductFeign {

    @GetMapping("/product/all")
    ResponseEntity<List<Product>> getAllProducts();

    @GetMapping("/product/{id}")
    ResponseEntity<?> getProductById(@PathVariable Long id);

    @PostMapping("/product/create")
    ResponseEntity<Product> createProduct(@Valid @RequestBody Product product);

    @PutMapping("/product/update/{id}")
    ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updateProduct);

    @DeleteMapping("/product/{id}")
    ResponseEntity<Boolean> deleteProduct(@PathVariable Long id);
}
