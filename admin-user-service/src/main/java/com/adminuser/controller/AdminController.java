package com.adminuser.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adminuser.entity.Category;
import com.adminuser.entity.Product;
import com.adminuser.feign.CategoryFeign;
import com.adminuser.feign.ProductFeign;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/admin")
@CrossOrigin(origins="*")
public class AdminController {
	 
	@Autowired
	private ProductFeign pf;
	
	@GetMapping("/product/all")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<Product>> getAllProducts() {
		return pf.getAllProducts();
				
	}
  
	@GetMapping("/product/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
		return pf.getProductById(id);
	}
	
	@PostMapping("/product/create")
	@PreAuthorize("hasAuthority('ADMIN')")
	  public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
		return pf.createProduct(product);
	}

	
	@PutMapping("/product/update/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updateProduct){
		return pf.updateProduct(id, updateProduct);
	}
	
 
    @DeleteMapping("/product/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id){
    	return pf.deleteProduct(id);
    }
    
    
    @Autowired
    private CategoryFeign categoryfeign;
    
    @GetMapping("/category/all")
	public ResponseEntity<List<Category>> getAllCategories(){
    	return categoryfeign.getAllCategories();
    			
    }

	@GetMapping("/category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long id){
		return categoryfeign.getCategoryById(id);
	}

	@PostMapping("/category/add")
	public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category)
	{
		return categoryfeign.createCategory(category);
	}

	@PutMapping("/category/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody Category categoryUpdate){
		return categoryfeign.updateCategory(id, categoryUpdate);
	}

	@DeleteMapping("/category/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
		return categoryfeign.deleteCategory(id);
	}
	

}