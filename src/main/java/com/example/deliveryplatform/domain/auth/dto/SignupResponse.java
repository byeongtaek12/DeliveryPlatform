package com.example.deliveryplatform.domain.auth.dto;

import com.example.deliveryplatform.domain.user.entity.User;

import lombok.Getter;

@Getter
public class SignupResponse {
	private Long id;

	private SignupResponse(Long id) {
		this.id = id;
	}

	public static SignupResponse from(User user) {
		return new SignupResponse(user.getId());
	}


}
