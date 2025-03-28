package com.shopcart.cartservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	@Id
	@GeneratedValue
	private Long id;
	@NotBlank(message = "Cart Description Cannot Be Empty")
	@Size(min = 10, max = 60, message = "Cart description must in Between 10 to 60 characters")
	private String description;
	@NotNull(message = "Toatl cannot be null")
	@PositiveOrZero(message = "Total must be zero or a positive value")
	private Double total;
}
