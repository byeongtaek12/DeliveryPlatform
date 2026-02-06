package com.example.deliveryplatform.common.exception.code;

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
	USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER_006", "존재하지 않는 유저입니다"),

	// jwt 에러
	INVALID_JWT_SUBJECT(HttpStatus.UNAUTHORIZED,  "JWT_001", "유효하지 않은 JWT SUBJECT입니다"),
	USERNAME_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT_002", "유저가 존재하지 않습니다."),
	MALFORMED_JWT(HttpStatus.UNAUTHORIZED, "JWT_003", "형식이 깨져있습니다"),
	EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "JWT_004", "만료되었습니다."),
	UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "JWT_005", "지원 하지 않는 토큰입니다"),
	INVALID_SIGNATURE_JWT(HttpStatus.UNAUTHORIZED, "JWT_006", "지원 하지 않는 토큰입니다"),
	INVALID_JWT(HttpStatus.UNAUTHORIZED, "JWT_007", "Jwt 관련 에러 발생 기타 내용은 로그를 확인해주세요"),

	// validation 에러
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "VALIDATION_999", "해당 제약사항에 맞춰 입력해주세요."),

	// server 에러
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_999", "서버 내부 오류가 발생했습니다.");


	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;
}
