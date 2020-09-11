package com.br.davifelipe.springjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.br.davifelipe.springjwt.services.EmailService;
import com.br.davifelipe.springjwt.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
