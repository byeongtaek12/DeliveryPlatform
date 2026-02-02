package com.example.deliveryplatform.common.jwt;

import java.io.IOException;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final RequestMatcher skipMatcher;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return skipMatcher.matches(request);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

	}
}
