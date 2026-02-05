package com.example.deliveryplatform.common.jwt;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.deliveryplatform.common.exception.code.ErrorCode;
import com.example.deliveryplatform.common.exception.customException.JwtAuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenParser {

	private final SecretKey key;

	public Claims extractClaims(String token) {
		try{
			return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (MalformedJwtException e) {
			throw new JwtAuthenticationException(e, ErrorCode.MALFORMED_JWT);
		} catch (ExpiredJwtException e) {
			throw new JwtAuthenticationException(e, ErrorCode.EXPIRED_JWT);
		} catch (UnsupportedJwtException e) {
			throw new JwtAuthenticationException(e, ErrorCode.UNSUPPORTED_JWT);
		} catch (SecurityException e) {
			throw new JwtAuthenticationException(e, ErrorCode.INVALID_SIGNATURE_JWT);
		} catch (IllegalArgumentException | JwtException e) {
			throw new JwtAuthenticationException(e, ErrorCode.INVALID_JWT);
		}
	}
}
