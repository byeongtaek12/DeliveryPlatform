package com.example.deliveryplatform;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.deliveryplatform.common.exception.BaseException;
import com.example.deliveryplatform.common.exception.code.ErrorCode;
import com.example.deliveryplatform.domain.auth.dto.SignupRequest;
import com.example.deliveryplatform.domain.auth.service.AuthService;
import com.example.deliveryplatform.domain.user.model.UserRole;
import com.example.deliveryplatform.domain.user.repository.UserRepository;

@SpringBootTest
@Transactional
public class AuthSignupIntegrationTest extends MySQLContainerBaseTest {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("회원가입 성공: DB에 사용자 저장된다")
	void signup_persists_user() {

		// given
		var request = new SignupRequest("byeongtaek12@gmail.com", "test1234", "문무겸비",
			"01012341234", "user");

		// when
		var response = authService.signup(request);

		// then
		then(response.getId()).isNotNull();

		var saved = userRepository.findById(response.getId()).orElseThrow();
		then(saved.getEmail()).isEqualTo("byeongtaek12@gmail.com");
		then(saved.getPassword()).isNotBlank();
		then(saved.getNickname()).isEqualTo("문무겸비");
		then(saved.getPhoneNumber()).isEqualTo("01012341234");
		then(saved.getRole()).isEqualTo(UserRole.USER);
	}

	@Test
	@DisplayName("회원가입 실패: 이메일이 중복")
	void signup_fail_emailConflict_user() {

		// given
		var request = new SignupRequest("byeongtaek12@gmail.com", "test1234", "문무겸비",
			"01012341234", "user");

		authService.signup(request);
		userRepository.flush();

		var request1 = new SignupRequest("byeongtaek12@gmail.com", "test1234", "문무겸비1",
			"01012345678", "user");

		// when &  then
		thenThrownBy(() -> authService.signup(request1))
			.isInstanceOfSatisfying(BaseException.class, ex ->
				then(ex.getErrorCode()).isEqualTo(ErrorCode.CONFLICT_EMAIL)
			);
	}

	@Test
	@DisplayName("회원가입 실패: 역할이 존재 하지 않음")
	void signup_fail_role_x_user() {

		// given
		var request = new SignupRequest("byeongtaek12@gmail.com", "test1234", "문무겸비",
			"01012341234", "use");

		// when &  then
		thenThrownBy(() -> authService.signup(request))
			.isInstanceOfSatisfying(BaseException.class, ex ->
				then(ex.getErrorCode()).isEqualTo(ErrorCode.USER_ROLE_BAD_REQUEST)
			);
	}


}
