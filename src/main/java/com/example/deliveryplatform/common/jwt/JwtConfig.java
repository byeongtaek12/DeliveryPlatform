package com.example.deliveryplatform.common.jwt;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@Configuration
public class JwtConfig {

	@Bean
	public SecretKey jwtSigningKey(JwtProperties props) {
		byte[] bytes;

		try {
			bytes = Base64.getDecoder().decode(props.secret().key());
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("JWT 비밀 키는 Base64로 인코딩되어야 합니다.", e);
		}

		if (bytes.length < 32) {
			throw new IllegalStateException("JWT secret is too short: minimum 32 bytes required");
		}

		return Keys.hmacShaKeyFor(bytes);
	}
}
