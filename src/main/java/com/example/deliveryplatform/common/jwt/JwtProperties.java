package com.example.deliveryplatform.common.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "jwt")
@Validated
public record JwtProperties(
	@NotBlank String issuer,
	@Valid @NotNull Secret secret,
	@Valid @NotNull AccessToken accessToken
) {
	public record Secret(@NotBlank String key) {}
	public record AccessToken(@Min(1) int expireMinutes){}
}