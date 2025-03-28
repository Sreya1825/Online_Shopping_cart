package com.shopcart.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "products")
public class Product {
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long productId;

	    @NotBlank(message = "Product type is mandatory")
	    private String productType;

	    @NotBlank(message = "Product name is mandatory")
	    private String productName;

	    @NotBlank(message = "Category is mandatory")
	    private String category;

	    @Min(value = 0, message = "Rating cannot be negative")
	    @Max(value = 5, message = "Rating cannot be more than 5")
	    private int rating;

	    @Min(value = 0, message = "Price must be a positive value")
	    @Digits(integer = 10, fraction = 2, message = "Price can have up to 10 digits before the decimal point and up to 2 fractional digits")
	    private BigDecimal price;

	    @NotBlank(message = "Description cannot be empty")
	    private String description;

	    @NotBlank(message = "Image URL cannot be blank")
	    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
	    private String image;

	    private int version;
	    
	    

		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}

		public String getProductType() {
			return productType;
		}

		public void setProductType(String productType) {
			this.productType = productType;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}
		
		

}