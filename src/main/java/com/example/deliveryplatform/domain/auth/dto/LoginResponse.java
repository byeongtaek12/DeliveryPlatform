package com.example.deliveryplatform.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
	private Long id;
	private String accessToken;

	private LoginResponse(Long id, String accessToken) {
		this.id = id;
		this.accessToken = accessToken;
	}

	public static LoginResponse of(Long id, String accessToken) {
		return new LoginResponse(id, accessToken);
	}


}
