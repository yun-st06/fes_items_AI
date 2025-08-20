package com.example.app.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class SignupForm {

	@NotBlank(message = " {signup.name.required}")
	@Size(max = 50, message = "{signup.name.max}")
	private String name;

	@NotBlank(message = "{signup.email.required}")
	@Email(message = "{signup.email.format}")
	private String email;

	@NotBlank(message = "{signup.password.required}")
	@Size(min = 8, message = "{signup.password.size}")
	private String password;

}
