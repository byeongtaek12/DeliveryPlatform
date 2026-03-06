package com.example.deliveryplatform.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "빈 값이 이메일이 될 순 없습니다.")
	private String email;
	@NotBlank(message = "빈 값이 패스워드가 될 순 없습니다.")
	private String password;
}
