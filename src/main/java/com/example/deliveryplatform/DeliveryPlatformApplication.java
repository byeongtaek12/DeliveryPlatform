package com.example.deliveryplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DeliveryPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryPlatformApplication.class, args);
	}

}
