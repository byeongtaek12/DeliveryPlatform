package com.example.deliveryplatform.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deliveryplatform.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByPhoneNumber(String phoneNumber);

}
