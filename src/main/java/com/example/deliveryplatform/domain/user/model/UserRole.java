package com.example.deliveryplatform.domain.user.model;

import com.example.deliveryplatform.common.exception.BaseException;
import com.example.deliveryplatform.common.exception.ErrorCode;

public enum UserRole {
	USER, OWNER;

	public static UserRole of(String role) {

		if (role == null) {
			throw new BaseException(ErrorCode.USER_ROLE_BAD_REQUEST);
		}

		String upperCaseRole = role.trim().toUpperCase();
		if (upperCaseRole.isEmpty()) {
			throw new BaseException(ErrorCode.USER_ROLE_BAD_REQUEST);
		}

		try {
			return UserRole.valueOf(upperCaseRole);
		} catch (IllegalArgumentException e) {
			throw new BaseException(ErrorCode.USER_ROLE_BAD_REQUEST);
		}
	}
}
