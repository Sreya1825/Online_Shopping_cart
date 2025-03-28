package com.shopcart.productservice.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopcart.productservice.dto.ProductDto;
import com.shopcart.productservice.entity.Product;
import com.shopcart.productservice.exception.CustomException;
import com.shopcart.productservice.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins="*")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	public ProductController(ProductService productService) {

		this.productService = productService;
	}

	@Autowired
	Environment environment;

	@Autowired
	ProductService productService;

	@GetMapping("/all")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> allProducts = productService.getAllProducts();
		return new ResponseEntity<>(allProducts, HttpStatus.OK);
	}

	 @GetMapping("/{id}")
	    public ResponseEntity<?> getProductById(@PathVariable Long id) {
	        Optional<Product> productId = productService.getProductById(id);

	        if (productId.isPresent()) {
	            Product product = productId.get();
	            return new ResponseEntity<>(product, HttpStatus.OK);
	        } else {
	            // Throw custom exception if product not found
	            String errorMessage = "Product not found with ID: " + id;
	            logger.error("Error: {} | Status: {}", errorMessage, HttpStatus.NOT_FOUND);
	            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
	        }
	    }

	  @GetMapping("/orderStatus/{id}/{orderId}")
	    public ResponseEntity<?> getProductOrderById(@PathVariable Long id, @PathVariable Long orderId) {
	        Optional<ProductDto> productId = productService.getProductById(id, orderId);

	        if (productId.isPresent()) {
	            ProductDto productDto = productId.get();
	            return new ResponseEntity<>(productDto, HttpStatus.OK);
	        } else {
	            // Throw custom exception if product not found
	            String errorMessage = "Product not found with ID: " + id;
	            logger.error("Error: {} | Status: {}", errorMessage, HttpStatus.NOT_FOUND);
	            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
	        }
	    }

	  @PostMapping("/create")
	  public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
	      try {
	          Product createdProduct = productService.createProduct(product);
	          return new ResponseEntity<>(createdProduct, HttpStatus.CREATED); // Return CREATED status
	      } catch (Exception e) {
	          String errorMessage = "Error creating product: " + e.getMessage();
	          logger.error("Error: {}", errorMessage);
	          throw new CustomException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	  }
	  
	  @GetMapping("/type/{type}")
	  public ResponseEntity<List<Product>> getProductByType(@PathVariable String type) {
	      List<Product> products = productService.getProductByType(type);
	      if (products.isEmpty()) {
	          String errorMessage = "No products found for type: " + type;
	          logger.error("Error: {} | Status: {}", errorMessage, HttpStatus.NOT_FOUND);
	          throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
	      }
	      return new ResponseEntity<>(products, HttpStatus.OK);
	  }

	  


	 @PutMapping("/update/{id}")
	    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updateProduct) {
	        Optional<Product> updatedProduct = productService.updateProduct(id, updateProduct);
	        if (updatedProduct.isPresent()) {
	            Product updated = updatedProduct.get();
	            return new ResponseEntity<>(updated, HttpStatus.OK);
	        } else {
	            String errorMessage = "Product not found with ID: " + id;
	            logger.error("Error: {} | Status: {}", errorMessage, HttpStatus.NOT_FOUND);
	            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
	        }
	    }
	 
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
	        if (!productService.getProductById(id).isPresent()) {
	            String errorMessage = "Product not found with ID: " + id;
	            logger.error("Error: {} | Status: {}", errorMessage, HttpStatus.NOT_FOUND);
	            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
	        }
	        productService.deleteProduct(id);
	        return ResponseEntity.ok(true);
	    }
}
