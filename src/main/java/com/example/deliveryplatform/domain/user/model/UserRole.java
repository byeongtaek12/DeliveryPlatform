package com.example.deliveryplatform.domain.user.model;

import com.example.deliveryplatform.common.exception.BaseException;
import com.example.deliveryplatform.common.exception.ErrorCode;

public enum UserRole {
	USER, OWNER;

	public static UserRole of(String role) {
		try {
			return UserRole.valueOf(role.trim().toUpperCase());
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new BaseException(ErrorCode.USER_ROLE_BAD_REQUEST);
		}
	}
}
