package com.example.deliveryplatform.common.jwt;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtConfig {

	@Bean
	public SecretKey jwtSigningKey(JwtProperties props) {
		byte[] bytes = Base64.getDecoder().decode(props.secret().key());
		return Keys.hmacShaKeyFor(bytes);
	}
}
