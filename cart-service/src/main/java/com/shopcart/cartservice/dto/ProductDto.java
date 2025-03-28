package com.shopcart.cartservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {
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
    
    @NotBlank(message = "Product status cannot be null")
    private String productStatus; 

	

	

   
}