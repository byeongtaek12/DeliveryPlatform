package com.example.deliveryplatform.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.deliveryplatform.common.exception.code.ErrorCode;
import com.example.deliveryplatform.common.exception.customException.BaseException;
import com.example.deliveryplatform.common.jwt.JwtTokenProvider;
import com.example.deliveryplatform.common.security.CustomUserDetails;
import com.example.deliveryplatform.domain.auth.dto.LoginRequest;
import com.example.deliveryplatform.domain.auth.dto.LoginResponse;
import com.example.deliveryplatform.domain.user.entity.User;
import com.example.deliveryplatform.domain.user.model.UserRole;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("로그인 성공")
	void loginSuccess() {

		// given
		LoginRequest loginRequest = new LoginRequest("byeongtaek12@gmail.com", "1234");

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

		User user = User.create("byeongtaek12@gmail.com", "test1234", "문무겸비",
			"010-1234-1234", "user");

		ReflectionTestUtils.setField(user, "id", 1L);

		CustomUserDetails customUserDetails = CustomUserDetails.fromLogin(user);

		Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());

		given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.willReturn(authentication);

		given(jwtTokenProvider.createToken(1L, UserRole.USER)).willReturn("Test- accessToken");

		// when
		LoginResponse response = authService.login(loginRequest);

		// then
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getAccessToken()).isEqualTo("Test- accessToken");
	}

	@Test
	@DisplayName("로그인 실패")
	void loginFail() {

		// given
		LoginRequest loginRequest = new LoginRequest("byeongtaek12@gmail.com", "1234");

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

		CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
		Authentication authentication = mock(Authentication.class);

		given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.willReturn(authentication);

		given(authentication.getPrincipal()).willReturn(customUserDetails);
		given(customUserDetails.getUserId()).willReturn(1L);
		given(customUserDetails.getAuthorities()).willReturn(List.of());

		// when && then
		BaseException baseException = assertThrows(BaseException.class, () -> authService.login(loginRequest));

		assertThat(baseException.getErrorCode()).isEqualTo(ErrorCode.AUTH_NO_ROLE);
	}


}