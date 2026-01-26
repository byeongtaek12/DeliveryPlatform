package com.example.deliveryplatform.domain.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {
	@Email
	private String email;
	private String password;
	private String nickname;
	private String phoneNumber;
	private String role;
}
