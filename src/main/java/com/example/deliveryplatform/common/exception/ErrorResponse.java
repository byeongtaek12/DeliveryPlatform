package com.example.deliveryplatform.common.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ErrorResponse {
	private HttpStatus httpStatus;
	private String errorCode;
	private String message;
	private Map<String, String> errors;

	public static ErrorResponse from(ErrorCode errorCode) {
		return ErrorResponse.builder()
			.httpStatus(errorCode.getHttpStatus())
			.errorCode(errorCode.getErrorCode())
			.message(errorCode.getMessage())
			.build();
	}

	public static ErrorResponse of(ErrorCode errorCode, Map<String, String> errors) {
		return ErrorResponse.builder()
			.httpStatus(errorCode.getHttpStatus())
			.errorCode(errorCode.getErrorCode())
			.message(errorCode.getMessage())
			.errors(errors)
			.build();
	}
}
