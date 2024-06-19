package com.example.Individual.Pan.Verification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class Panconfig {

	
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
