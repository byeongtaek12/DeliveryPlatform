package com.example.deliveryplatform.common.security;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.deliveryplatform.domain.user.entity.User;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails, CredentialsContainer {

	@Getter
	private final Long userId;
	private final String email;
	private String password;
	private final boolean enabled;
	private final Collection<? extends GrantedAuthority> authorities;

	private CustomUserDetails(Long userId, String email, String password, boolean enabled,
		Collection<? extends GrantedAuthority> authorities) {
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.authorities = authorities;
	}

	public static CustomUserDetails fromLogin(User user) {
		return new CustomUserDetails(
			user.getId(),
			user.getEmail(),
			user.getPassword(),
			user.isActive(),
			List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
		);
	}

	public static CustomUserDetails fromJwt(User user) {
		return new CustomUserDetails(
			user.getId(),
			user.getEmail(),
			null,
			user.isActive(),
			List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
		);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public @Nullable String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
	}
}
