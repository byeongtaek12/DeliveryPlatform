package com.example.deliveryplatform.common.jwt;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.deliveryplatform.common.security.CustomUserDetailsService;
import com.example.deliveryplatform.domain.user.model.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

	@Mock
	private JwtProperties props;

	@Mock
	private SecretKey key;

	@Mock
	private JwtTokenParser tokenParser;

	@Mock
	private CustomUserDetailsService customUserDetailsService;

	@InjectMocks
	JwtTokenProvider tokenProvider;

	@BeforeEach
	void setUp() {
		props = new JwtProperties(
			"DeliveryPlatform",
			// test용 랜덤 키
			new JwtProperties.Secret("rxVnENG7E+y8ywLhlMWIzBZnE///x0AjXU3JedsCc8pgwXO/NCODZd6IUE5dWEaL5ehIYM7OlmJCvFfy0CDVVA=="),
			new JwtProperties.AccessToken(10)
		);

		key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(props.secret().key()));
		tokenParser = new JwtTokenParser(key);
		tokenProvider = new JwtTokenProvider(props, key, tokenParser, customUserDetailsService);
	}


	@Test
	@DisplayName("만료시간 & claim & subject 잘 들어갔는지 확인")
	void createToken() {

		// given
		Long userId = 1L;
		UserRole userRole = UserRole.USER;

		// when
		String token = tokenProvider.createToken(userId, userRole);

		// then
		Claims extractClaims = tokenParser.extractClaims(token);

		assertThat(extractClaims.get("userRole", String.class)).isEqualTo("USER");
		assertThat(extractClaims.getSubject()).isEqualTo("1");

		long exp = extractClaims.getExpiration().getTime();
		long iat = extractClaims.getIssuedAt().getTime();
		long diffMills = exp - iat;
		long expectedMillis = Duration.ofMinutes(props.accessToken().expireMinutes()).toMillis();

		assertThat(diffMills).isBetween(expectedMillis - 1000, expectedMillis + 1000);

	}

	@Test
	void getAuthentication() {
	}
}