package com.example.deliveryplatform.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.deliveryplatform.common.exception.customException.BaseException;
import com.example.deliveryplatform.common.exception.code.ErrorCode;
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
		User foundUser = userRepository.findById(id).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

		return CustomUserDetails.fromJwt(foundUser);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User foundUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("해당 email을 가진 유저가 없습니다."));

		return CustomUserDetails.fromLogin(foundUser);
	}
}
