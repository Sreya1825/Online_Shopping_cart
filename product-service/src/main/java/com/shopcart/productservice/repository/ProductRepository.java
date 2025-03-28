package com.shopcart.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopcart.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	
	List<Product> findByProductType(String productType);

	

}
