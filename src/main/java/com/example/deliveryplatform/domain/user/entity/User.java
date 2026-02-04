package com.example.deliveryplatform.domain.user.entity;

import java.time.LocalDateTime;

import com.example.deliveryplatform.common.BaseEntity;
import com.example.deliveryplatform.domain.auth.dto.SignupRequest;
import com.example.deliveryplatform.domain.user.model.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "users",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_users_email", columnNames = "email"),
		@UniqueConstraint(name = "uk_users_nickname", columnNames = "nickname"),
		@UniqueConstraint(name = "uk_users_phoneNumber", columnNames = "phoneNumber")
	}
	)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;

	private LocalDateTime deletedAt;

	@Builder
	private User(Long userId, String email, String password, String nickname, String phoneNumber, UserRole role) {
		this.id = userId;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	public static User create(String email, String encodedPassword, String nickname,
		String phoneNumber, String role) {
		return User.builder()
			.email(email)
			.password(encodedPassword)
			.nickname(nickname)
			.phoneNumber(phoneNumber)
			.role(UserRole.of(role))
			.build();
	}

	public boolean isActive() {
		return deletedAt == null;
	}
}
