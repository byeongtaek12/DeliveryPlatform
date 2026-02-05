package com.example.deliveryplatform.common.jwt;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.deliveryplatform.common.security.CustomAuthenticationEntryPoint;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final RequestMatcher skipMatcher;
	private final JwtTokenExtractor tokenExtractor;
	private final JwtTokenProvider tokenProvider;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;


	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return skipMatcher.matches(request);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try {
			String token = tokenExtractor.extract(request);

			if (StringUtils.hasText(token)) {
				Authentication authentication = tokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			filterChain.doFilter(request,response);
		} catch (AuthenticationException e) {
			SecurityContextHolder.clearContext();
			authenticationEntryPoint.commence(request, response, e);
			return;
		}
	}
}
