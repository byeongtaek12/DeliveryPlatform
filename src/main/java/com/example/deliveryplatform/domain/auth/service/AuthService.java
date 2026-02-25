package com.example.deliveryplatform.domain.auth.service;

import java.util.List;
import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.deliveryplatform.common.exception.customException.BaseException;
import com.example.deliveryplatform.common.exception.code.ErrorCode;
import com.example.deliveryplatform.common.jwt.JwtTokenProvider;
import com.example.deliveryplatform.common.security.CustomUserDetails;
import com.example.deliveryplatform.domain.auth.dto.LoginRequest;
import com.example.deliveryplatform.domain.auth.dto.LoginResponse;
import com.example.deliveryplatform.domain.auth.dto.SignupRequest;
import com.example.deliveryplatform.domain.auth.dto.SignupResponse;
import com.example.deliveryplatform.domain.user.entity.User;
import com.example.deliveryplatform.domain.user.model.UserRole;
import com.example.deliveryplatform.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	public SignupResponse signup(SignupRequest signupRequest) {

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			throw new BaseException(ErrorCode.CONFLICT_EMAIL);
		}

		if (userRepository.existsByNickname(signupRequest.getNickname())) {
			throw new BaseException(ErrorCode.CONFLICT_NICKNAME);
		}

		if (userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
			throw new BaseException(ErrorCode.CONFLICT_PHONENUMBER);
		}

		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

		User user = User.create(signupRequest.getEmail(), encodedPassword, signupRequest.getNickname()
		, signupRequest.getPhoneNumber(), signupRequest.getRole());


		User savedUser = userRepository.save(user);

		return SignupResponse.from(savedUser);
	}

	public LoginResponse login(LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

		Authentication authenticated = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		CustomUserDetails principal = (CustomUserDetails)authenticated.getPrincipal();

		Long userId = principal.getUserId();

		List<UserRole> role = principal.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.filter(Objects::nonNull)
			.map(auth -> auth.replace("ROLE_", ""))
			.map(UserRole::of)
			.toList();

		String accessToken = jwtTokenProvider.createToken(userId, role.get(0));

		return LoginResponse.of(userId, accessToken);
	}

}
