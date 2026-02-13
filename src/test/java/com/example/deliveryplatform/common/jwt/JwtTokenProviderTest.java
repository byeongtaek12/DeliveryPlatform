package com.example.deliveryplatform.common.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.deliveryplatform.common.security.CustomUserDetails;
import com.example.deliveryplatform.common.security.CustomUserDetailsService;
import com.example.deliveryplatform.domain.user.entity.User;
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
	@DisplayName("getAuthentication 성공: 실제 토큰으로 subject(userId) 파싱 후 UserDetails 로드 + Authentication 생성")
	void getAuthentication_success_realToken() {
		// given
		Long userId = 1L;
		UserRole userRole = UserRole.USER;

		String token = tokenProvider.createToken(userId, userRole);

		UserDetails userDetails = CustomUserDetails.fromJwt(
			User.create(
				"byeongtaek12@gmail.com",
				"encodedPw",          // 더미
				"문무겸비",
				"01029818699",
				"user"
			)
		);

		given(customUserDetailsService.loadUserById(1L)).willReturn(userDetails);

		// when
		Authentication authentication = tokenProvider.getAuthentication(token);

		// then
		assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
		assertThat(authentication.getPrincipal()).isSameAs(userDetails);
		assertThat(authentication.getCredentials()).isNull();

		assertThat(authentication.getAuthorities())
			.extracting(GrantedAuthority::getAuthority)
			.containsExactlyElementsOf(
				userDetails.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.toList()
			);

		assertThat(authentication.isAuthenticated()).isTrue();
		verify(customUserDetailsService).loadUserById(1L);
	}
}