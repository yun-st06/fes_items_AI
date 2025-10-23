package com.example.app.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm {

	@NotBlank(message = "{login.email.required}")
	@Email(message = "{login.email.format}")
	private String email;

	@NotBlank(message = "{login.password.required}")
	@Size(min = 8, message = "{login.password.size}")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{signup.password.pattern}") // 半角英数字のみ
	private String password;

}
