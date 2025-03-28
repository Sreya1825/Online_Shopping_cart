package com.shopcart.categoryservice.dto;

import java.util.List;

public class CategoryDto {
	private Long id;
	private String name;
	private String slug;
	private List<ProductDto> productDto;
	private String status;
	private String productMessage;
	

	public String getProductMessage() {
		return productMessage;
	}

	public void setProductMessage(String productMessage) {
		this.productMessage = productMessage;
	}

	public Long getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	
	public List<ProductDto> getProductDto() {
		return productDto;
	}

	public void setProductDto(List<ProductDto> productDto) {
		this.productDto = productDto;
	}

	
}
