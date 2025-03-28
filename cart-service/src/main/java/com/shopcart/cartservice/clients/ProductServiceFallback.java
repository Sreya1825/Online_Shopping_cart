package com.shopcart.cartservice.clients;

import org.springframework.stereotype.Component;

import com.shopcart.cartservice.dto.ProductDto;



@Component
public class ProductServiceFallback implements ProductFeginClient  {

	@Override
	public ProductDto getProductById(Long id) {
		ProductDto productDto = new ProductDto();
        productDto.setProductId(id);
        productDto.setProductName("Product Service is currently unavailable" );
        return productDto;
		
	}

}
