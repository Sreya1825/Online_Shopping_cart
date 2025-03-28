package com.shopcart.categoryservice.clients;

import org.springframework.stereotype.Component;

import com.shopcart.categoryservice.dto.ProductDto;

@Component
public class ProductServiceFallback implements ProductFeginClient  {

	@Override
	public ProductDto getProductById(Long id) {
		ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName("Product Service is currently unavailable" );
        return productDto;
		
	}

}
