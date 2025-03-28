package com.shopcart.categoryservice.error;

import com.shopcart.categoryservice.dto.ProductDto;
import com.shopcart.categoryservice.entity.Category;

public class CategoryReponse {
	private ProductDto productDto;
	private Category category;

	public CategoryReponse() {

	}

	public ProductDto getProductDto() {
		return productDto;
	}

	public void setProductDto(ProductDto productDto) {
		this.productDto = productDto;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
