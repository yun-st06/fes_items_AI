package com.example.app.form;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
@Data
public class ItemForm {
	
	
	private Integer id;
	
	
	@NotBlank(message ="{item.name.required}")
	@Size(max =50,message= "{item.name.max}")
	private String itemName;
	
	
	
	@NotBlank(message ="{item.category.required}")
	private String category;
	
	
	@NotNull(message="{item.quantity.required}")
    private Integer quantity;
	
	@NotNull(message="{item.festivalId.required}")
	private Integer festivalId;
	
}
