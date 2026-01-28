package com.example.deliveryplatform.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.deliveryplatform.common.exception.BaseException;
import com.example.deliveryplatform.common.exception.ErrorCode;
import com.example.deliveryplatform.domain.auth.dto.SignupRequest;
import com.example.deliveryplatform.domain.auth.dto.SignupResponse;
import com.example.deliveryplatform.domain.user.entity.User;
import com.example.deliveryplatform.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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

}
