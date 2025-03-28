package com.adminuser.entity;


import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="categ")
public class Category {
	@Id
	@GeneratedValue
	private Long id;
	
	
	@NotBlank(message="Name is mandatory")
	private String name;
	
	@NotBlank(message = "Slug cannot be blank")
	private String slug;
	
	@ElementCollection
   @CollectionTable(name = "category_products", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "product_id")
	private List<Long> productId;
	
	
}
