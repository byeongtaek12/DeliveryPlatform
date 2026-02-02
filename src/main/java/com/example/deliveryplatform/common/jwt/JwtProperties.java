package com.example.deliveryplatform.common.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
	@NotBlank String issuer,
	@Valid Secret secret,
	@Valid AccessToken accessToken
) {
	public record Secret(@NotBlank String key) {}
	public record AccessToken(@Min(1) int expireMinutes){}
}