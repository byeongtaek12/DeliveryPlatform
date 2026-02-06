package com.example.deliveryplatform.common.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.deliveryplatform.common.exception.customException.BaseException;
import com.example.deliveryplatform.common.exception.code.ErrorCode;
import com.example.deliveryplatform.common.exception.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.from(e.getErrorCode()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("Unhandled exception", e);

		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse body = ErrorResponse.from(errorCode);
		return ResponseEntity.status(body.getHttpStatus()).body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();

		e.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError)error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, errors);
		return ResponseEntity.status(400).body(response);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {

		ConstraintViolationException cve = findHibernateConstraintViolation(e);
		if (cve != null) {
			String constraintName = cve.getConstraintName();
			if (constraintName != null) {
				if (constraintName.contains("uk_users_email")) {
					return conflict(ErrorCode.CONFLICT_EMAIL);
				}

				if (constraintName.contains("uk_users_nickname")) {
					return conflict(ErrorCode.CONFLICT_NICKNAME);
				}

				if (constraintName.contains("uk_users_phone")) {
					return conflict(ErrorCode.CONFLICT_PHONENUMBER);
				}
			}
		}

		return conflict(ErrorCode.CONFLICT_USER_DATA);
	}

	private ResponseEntity<ErrorResponse> conflict(ErrorCode errorCode) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.from(errorCode));
	}

	private ConstraintViolationException findHibernateConstraintViolation(Throwable t) {
		while (t != null) {
			if (t instanceof ConstraintViolationException cve) return cve;
			t = t.getCause();
		}
		return null;
	}

}
