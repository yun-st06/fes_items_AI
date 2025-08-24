package com.example.app.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FesMemoForm {
        
	 private Integer id;
	 
	 @NotNull(message = "{memo.year.required}")
	 private Integer memoYear;
	 
	 private Integer festivalId;
	 
	 @NotBlank(message = "{memo.text.required}")
	 @Size(max = 200,message ="{memo.text.max}")
     private String memoText;	 
  }
