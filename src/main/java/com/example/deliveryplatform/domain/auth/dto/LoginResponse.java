package com.example.deliveryplatform.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
	private Long id;
	private String accessToken;
}
