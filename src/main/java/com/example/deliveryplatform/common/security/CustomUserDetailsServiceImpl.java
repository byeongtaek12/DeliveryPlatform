package com.example.deliveryplatform.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.deliveryplatform.common.exception.code.ErrorCode;
import com.example.deliveryplatform.common.exception.customException.JwtAuthenticationException;
import com.example.deliveryplatform.domain.user.entity.User;
import com.example.deliveryplatform.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService{
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserById(Long id) {
		User foundUser = userRepository.findById(id)
			.orElseThrow(() -> new JwtAuthenticationException(ErrorCode.USERNAME_NOT_FOUND));

		return CustomUserDetails.fromJwt(foundUser);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User foundUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

		return CustomUserDetails.fromLogin(foundUser);
	}
}
