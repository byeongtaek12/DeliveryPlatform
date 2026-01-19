package com.example.deliveryplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DeliveryPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryPlatformApplication.class, args);
	}

}
