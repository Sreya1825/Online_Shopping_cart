package com.shopcart.categoryservice.service;

import java.util.ArrayList;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shopcart.categoryservice.clients.ProductFeginClient;
import com.shopcart.categoryservice.dto.CategoryDto;
import com.shopcart.categoryservice.dto.ProductDto;
import com.shopcart.categoryservice.entity.Category;
import com.shopcart.categoryservice.exception.CustomException;
import com.shopcart.categoryservice.repository.CategoryRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
@SuppressWarnings("unused")
public class CategoryService {
	 private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
	@Autowired
	ProductFeginClient productFeginClient;

	@Autowired
	CategoryRepository categoryRepository;

	public List<Category> getAllCategories(Category category) {
		return categoryRepository.findAll();
	}
	
	@CircuitBreaker(name = "categoryServiceProductCircuitBreaker", fallbackMethod = "productFallBackMethod")
	public Optional<CategoryDto> getCategoryById(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> 
			new CustomException("Category not found with ID: " + id, HttpStatus.NOT_FOUND));

		List<ProductDto> products = new ArrayList<>();
		for (Long productId : category.getProductId()) {
			ProductDto product = productFeginClient.getProductById(productId);
			products.add(product);
		}
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(category.getId());
		categoryDto.setName(category.getName());
		categoryDto.setSlug(category.getSlug());
		categoryDto.setProductDto(products);
		categoryDto.setStatus("Successfully fetched products..");
		categoryDto.setProductMessage("Products are available for this category");
		return Optional.of(categoryDto);
	}

	public Optional<CategoryDto> productFallBackMethod(Long id, Exception e) {
		log.warn("Fallback method invoked due to: {}", e.getMessage());
		Optional<Category> categoryId = categoryRepository.findById(id);
		if (categoryId.isPresent()) {
			Category category = categoryId.get();
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setId(category.getId());
			categoryDto.setName(category.getName());
			categoryDto.setSlug(category.getSlug());

			categoryDto.setStatus("Product Service is unavailable or down, please try again later..");
			categoryDto.setProductMessage("No products available for this category at the moment.");
			return Optional.of(categoryDto);
		} else {
			throw new CustomException("Category not found during fallback with ID: " + id, HttpStatus.NOT_FOUND);
		}
	}

	public Category createCategory(Category category) {
		if (category == null) {
			throw new CustomException("Invalid category data", HttpStatus.BAD_REQUEST);
		}
		return categoryRepository.save(category);
	}

	public Optional<Category> updateCategory(Long id, Category categoryUpdate) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> 
			new CustomException("Category not found with ID: " + id, HttpStatus.NOT_FOUND));

		category.setName(categoryUpdate.getName());
		category.setSlug(categoryUpdate.getSlug());
		category.setProductId(categoryUpdate.getProductId());
		categoryRepository.save(category);
		return Optional.of(category);
	}

	public boolean deleteCategory(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> 
	    new CustomException("Category not found with ID: " + id, HttpStatus.NOT_FOUND));
		categoryRepository.deleteById(id);
		return true;
	}

}
