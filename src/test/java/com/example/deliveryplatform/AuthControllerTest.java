package com.example.deliveryplatform;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.deliveryplatform.common.exception.code.ErrorCode;
import com.example.deliveryplatform.common.exception.customException.BaseException;
import com.example.deliveryplatform.domain.auth.controller.AuthController;
import com.example.deliveryplatform.domain.auth.dto.LoginRequest;
import com.example.deliveryplatform.domain.auth.dto.LoginResponse;
import com.example.deliveryplatform.domain.auth.dto.SignupRequest;
import com.example.deliveryplatform.domain.auth.dto.SignupResponse;
import com.example.deliveryplatform.domain.auth.service.AuthService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	AuthService authService;

	@Test
	void createUserReturn201AndBody() throws Exception {
		SignupRequest req = new SignupRequest(
			"byeongtaek12@gmail.com",
			"1234",
			"문무겸비",
			"010-1234-1234",
			"user");

		SignupResponse res = SignupResponse.of(1L);

		given(authService.signup(ArgumentMatchers.any(SignupRequest.class)))
			.willReturn(res);

		mockMvc.perform(post("/api/auth/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isCreated())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(1L));
	}

	@Test
	void validationFailReturn400() throws Exception {
		SignupRequest req = new SignupRequest(
			"byeongtaek12",
			"1234",
			"하이",
			"010-1234-1234",
			"user");

		mockMvc.perform(post("/api/auth/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void customExceptionActionOk() throws Exception {
		SignupRequest req = new SignupRequest(
			"byeongtaek12@gmail.com",
			"1234",
			"문무겸비",
			"010-1234-1234",
			"user");

		given(authService.signup(ArgumentMatchers.any(SignupRequest.class)))
			.willThrow(new BaseException(ErrorCode.CONFLICT_EMAIL));

		mockMvc.perform(post("/api/auth/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.errorCode").value("USER_001"))
			.andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
	}

	@Test
	void loginSuccess() throws Exception {
		LoginRequest req = new LoginRequest(
			"byeongtaek12@gmail.com",
			"1234"
		);

		LoginResponse res = LoginResponse.of(1L, "testToken");

		given(authService.login(any(LoginRequest.class))).willReturn(res);

		mockMvc.perform(post("/api/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.accessToken").value("testToken"));
	}

	@Test
	void loginFailWrongPassword() throws Exception {
		LoginRequest req = new LoginRequest(
			"byeongtaek12@gmail.com",
			"1"
		);

		given(authService.login(any(LoginRequest.class))).willThrow(new BaseException(ErrorCode.INVALID_CREDENTIALS));

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isUnauthorized());
	}

	@Test
	void loginFailValidation() throws Exception {
		LoginRequest req = new LoginRequest(
			"byeongtaek12@gmail.com",
			""
		);

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isBadRequest());
	}



}
