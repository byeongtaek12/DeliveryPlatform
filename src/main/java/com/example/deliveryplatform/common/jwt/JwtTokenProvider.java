package com.example.deliveryplatform.common.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.deliveryplatform.common.exception.code.ErrorCode;
import com.example.deliveryplatform.common.exception.customException.JwtAuthenticationException;
import com.example.deliveryplatform.common.security.CustomUserDetailsService;
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
	private final CustomUserDetailsService customUserDetailsServiceImpl;

	public String createToken(Long userId, UserRole userRole) {
		Date now = new Date();
		Instant instant = now.toInstant().plus(Duration.ofMinutes(props.accessToken().expireMinutes()));
		Date exp = Date.from(instant);

		return Jwts.builder()
			.subject(String.valueOf(userId))
			.claim("userRole", userRole.name())
			.issuedAt(now)
			.expiration(exp)
			.signWith(key)
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = jwtTokenParser.extractClaims(accessToken);

		String subject = claims.getSubject();
		if (!StringUtils.hasText(subject)) {
			throw new JwtAuthenticationException(ErrorCode.INVALID_JWT_SUBJECT);
		}

		Long userId;
		try {
			userId = Long.valueOf(subject);
		} catch (NumberFormatException e) {
			throw new JwtAuthenticationException(e, ErrorCode.INVALID_JWT_SUBJECT);
		}
		UserDetails userDetails = customUserDetailsServiceImpl.loadUserById(userId);

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
