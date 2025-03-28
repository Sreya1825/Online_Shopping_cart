package com.shopcart.cartservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopcart.cartservice.entity.CartItems;
@Repository
public interface CartRepository extends JpaRepository<CartItems,Long>{

	List<CartItems> findByUserId(Long userId);
    List<CartItems> findByProductId(Long productId);
}


