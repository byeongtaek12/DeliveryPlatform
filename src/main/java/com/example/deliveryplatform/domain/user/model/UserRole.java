package com.example.deliveryplatform.domain.user.model;

import java.util.Optional;

import com.example.deliveryplatform.common.exception.customException.BaseException;
import com.example.deliveryplatform.common.exception.code.ErrorCode;

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

	public static Optional<UserRole> fromAuthority(String authority) {
		if (authority == null || authority.isBlank()) return Optional.empty();

		if (authority.startsWith("ROLE_")) {
			String name = authority.substring("ROLE_".length());
			return Optional.of(UserRole.of(name));
		}

		return Optional.empty();
	}
}
