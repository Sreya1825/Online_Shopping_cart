package com.shopcart.categoryservice.controller;

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

import com.shopcart.categoryservice.dto.CategoryDto;
import com.shopcart.categoryservice.entity.Category;
import com.shopcart.categoryservice.error.ErrorResponse;
import com.shopcart.categoryservice.exception.CustomException;
import com.shopcart.categoryservice.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category") 
@CrossOrigin(origins="*")
public class CategoryController {
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	public
	Environment environment;

	@GetMapping("/port")  
	public String portNumberStatus() {
		return "CategoryService running port number is :" + " " + environment.getProperty("local.server.port");

	}

	@GetMapping("/all")
	public ResponseEntity<List<Category>> getAllCategories() {
		List<Category> allCategories = categoryService.getAllCategories(null);
		return new ResponseEntity<>(allCategories, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
		try {
			Optional<CategoryDto> categoryId = categoryService.getCategoryById(id);
			if (categoryId.isPresent()) {
				CategoryDto category = categoryId.get();
				return new ResponseEntity<>(category, HttpStatus.OK);
			} else {
				throw new CustomException("Category not found with ID: " + id, HttpStatus.NOT_FOUND);
			}
		} catch (CustomException e) {
			logger.error("Error: {} | Status: {} ", e.getMessage(), e.getStatus());
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getStatus());
			return new ResponseEntity<>(errorResponse, e.getStatus());
		}
	}

	@PostMapping("/add")
	public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
		Category categor = categoryService.createCategory(category);
		return new ResponseEntity<>(categor, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody Category categoryUpdate) {
		try {
			Optional<Category> categoryId = categoryService.updateCategory(id, categoryUpdate);
			if (categoryId.isPresent()) {
				Category updatedCategory = categoryId.get();
				return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
			} else {
				throw new CustomException("Category not found with ID: " + id, HttpStatus.NOT_FOUND);
			}
		} catch (CustomException e) {
			logger.error("Error: {} | Status: {} ", e.getMessage(), e.getStatus());
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getStatus());
			return new ResponseEntity<>(errorResponse, e.getStatus());
		}
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<ErrorResponse> deleteCategory(@PathVariable Long id) {
	    try {
	        categoryService.deleteCategory(id);
	        ErrorResponse successResponse = new ErrorResponse("Deleted category successfully", HttpStatus.OK);
	        return new ResponseEntity<>(successResponse, HttpStatus.OK);
	    } catch (CustomException e) {
	        logger.error("Error: {} | Status: {} ", e.getMessage(), e.getStatus());
	        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getStatus());
	        return new ResponseEntity<>(errorResponse, e.getStatus());
	    }
	}


}
