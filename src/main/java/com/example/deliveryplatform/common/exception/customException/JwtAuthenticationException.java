package com.example.deliveryplatform.common.exception.customException;

import org.springframework.security.core.AuthenticationException;

import com.example.deliveryplatform.common.exception.code.ErrorCode;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

  private final ErrorCode errorCode;

  public JwtAuthenticationException(Throwable cause, ErrorCode errorCode) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
  }

  public JwtAuthenticationException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

}
