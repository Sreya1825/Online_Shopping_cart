package com.shopcart.productservice.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shopcart.productservice.clients.OrderServiceClient;
import com.shopcart.productservice.dto.ProductDto;
import com.shopcart.productservice.entity.Product;
import com.shopcart.productservice.exception.CustomException;
import com.shopcart.productservice.repository.ProductRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	@Autowired
	OrderServiceClient orderServiceClient;
	@Autowired
	ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}


	public Product createProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            String errorMessage = "Error creating product: " + e.getMessage();
            logger.error(errorMessage);
            throw new CustomException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Product> getProductById(Long id) {
        Optional<Product> productId = productRepository.findById(id);
        if (productId.isPresent()) {
            return productId;
        } else {
            String errorMessage = "Product with ID " + id + " not found";
            logger.warn(errorMessage);
            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    @CircuitBreaker(name = "productServiceOrderBreaker", fallbackMethod = "orderBreakerfallBackMethod")
    public Optional<ProductDto> getProductById(Long id, Long orderId) {
        Optional<Product> productId = productRepository.findById(id);
        if (productId.isPresent()) {
            Product product = productId.get();
            ProductDto productDto = new ProductDto();
            productDto.setProduct(product);

            String status = orderServiceClient.getOrderStatus(orderId);
            productDto.setOrderStatus(status);
            return Optional.of(productDto);
        } else {
            String errorMessage = "Product with ID " + id + " not found";
            logger.warn(errorMessage);
            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    public Optional<ProductDto> orderBreakerfallBackMethod(Long id, Long orderId, Exception e) {
        Optional<Product> productId = productRepository.findById(id);
        if (productId.isPresent()) {
            Product product = productId.get();
            ProductDto productDto = new ProductDto();
            productDto.setProduct(product);
            productDto.setOrderStatus("Order status is unavailable");
            return Optional.of(productDto);
        } else {
            String errorMessage = "Product with ID " + id + " not found during fallback";
            logger.warn(errorMessage);
            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    public Optional<Product> updateProduct(Long id, Product updateProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            
            updatedProduct.setProductType(updateProduct.getProductType());
            updatedProduct.setProductName(updateProduct.getProductName());
            updatedProduct.setCategory(updateProduct.getCategory());
            updatedProduct.setRating(updateProduct.getRating());
            updatedProduct.setPrice(updateProduct.getPrice());
            updatedProduct.setDescription(updateProduct.getDescription());
            updatedProduct.setImage(updateProduct.getImage());

            return Optional.of(productRepository.save(updatedProduct));
        } else {
            String errorMessage = "Product with ID " + id + " not found for update";
            logger.warn(errorMessage);
            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
        }
    }


//    public boolean deleteProduct(Long id) {
//        if (!productRepository.existsById(id)) {
//            String errorMessage = "Product with ID " + id + " not found for deletion";
//            logger.warn(errorMessage);
//            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
//        }
//        productRepository.deleteById(id);
//        return true;
//    }
    
    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            String errorMessage = "Product with ID " + id + " not found for deletion";
            logger.warn(errorMessage);
            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
        }
        
        productRepository.deleteById(id);
        return "Product with ID " + id + " deleted successfully";
    }

    public List<Product> getProductByType(String type) {
        List<Product> products = productRepository.findByProductType(type);
        if (products.isEmpty()) {
            String errorMessage = "No products found for type: " + type;
            logger.warn(errorMessage);
            throw new CustomException(errorMessage, HttpStatus.NOT_FOUND);
        }
        return products;
    }

    
    

}
