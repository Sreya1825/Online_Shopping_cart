package com.shopcart.categoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopcart.categoryservice.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
	
	

}
