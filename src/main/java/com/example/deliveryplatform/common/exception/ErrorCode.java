package com.example.deliveryplatform.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	CONFLICT_EMAIL(HttpStatus.CONFLICT, "USER_001", "이미 존재하는 이메일입니다."),
	CONFLICT_NICKNAME(HttpStatus.CONFLICT, "USER_002", "이미 존재하는 닉네임입니다."),
	CONFLICT_PHONENUMBER(HttpStatus.CONFLICT, "USER_003", "이미 존재하는 번호입니다."),
	CONFLICT_USER_DATA(HttpStatus.CONFLICT, "USER_004", "이미 존재하는 데이터입니다" ),
	USER_ROLE_BAD_REQUEST(HttpStatus.BAD_REQUEST,"USER_005", "없는 역할입니다"),
	// validation 에러
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "VALIDATION_999", "해당 제약사항에 맞춰 입력해주세요."),

	// server 에러
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_999", "서버 내부 오류가 발생했습니다.");


	private final HttpStatus httpStatus;
	private final String ErrorCode;
	private final String message;
}
