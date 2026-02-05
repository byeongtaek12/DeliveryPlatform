package com.example.deliveryplatform.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.example.deliveryplatform.common.jwt.JwtFilter;
import com.example.deliveryplatform.common.jwt.JwtTokenExtractor;
import com.example.deliveryplatform.common.jwt.JwtTokenProvider;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public RequestMatcher publicEndpoints() {
		var p = PathPatternRequestMatcher.withDefaults().basePath("/api");

		return new OrRequestMatcher(
			p.matcher("/auth/**")
		);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, Filter jwtFilter, RequestMatcher publicEndpoints) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sm ->
				sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(publicEndpoints)
				.permitAll()
				.anyRequest().authenticated())
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public JwtFilter jwtFilter(RequestMatcher publicEndPoints, JwtTokenExtractor jwtTokenExtractor,
		JwtTokenProvider jwtTokenProvider) {
		return new JwtFilter(publicEndPoints, jwtTokenExtractor, jwtTokenProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
