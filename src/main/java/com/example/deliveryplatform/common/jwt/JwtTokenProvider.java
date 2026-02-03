package com.example.deliveryplatform.common.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.deliveryplatform.common.security.CustomUserDetailService;
import com.example.deliveryplatform.common.security.CustomUserDetailsServiceImpl;
import com.example.deliveryplatform.domain.user.model.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final JwtProperties props;
	private final SecretKey key;
	private final JwtTokenParser jwtTokenParser;
	private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

	public String createToken(Long userId, UserRole userRole) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + props.accessToken().expireMinutes());

		return Jwts.builder()
			.subject(String.valueOf(userId))
			.claim("userRole", userRole.name())
			.issuedAt(now)
			.expiration(exp)
			.signWith(key)
			.compact();
	}
}
