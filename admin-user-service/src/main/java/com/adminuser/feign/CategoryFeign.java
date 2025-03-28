package com.adminuser.feign;


import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.adminuser.entity.Category;

import jakarta.validation.Valid;

@FeignClient("CATEGORYSERVICE")
public interface CategoryFeign {

	
	
	@GetMapping("/category/all")
	public ResponseEntity<List<Category>> getAllCategories();

	@GetMapping("/category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long id);

	@PostMapping("/category/add")
	public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category);

	@PutMapping("/category/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody Category categoryUpdate);

	@DeleteMapping("/category/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id) ;
	

}
