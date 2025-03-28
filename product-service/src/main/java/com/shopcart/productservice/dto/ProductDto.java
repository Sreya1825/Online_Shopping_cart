
package com.shopcart.productservice.dto;

import com.shopcart.productservice.entity.Product;



public class ProductDto {
	private Product product;
	private String orderStatus;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
}
