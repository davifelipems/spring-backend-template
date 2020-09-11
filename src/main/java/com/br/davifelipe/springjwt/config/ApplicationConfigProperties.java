package com.br.davifelipe.springjwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationConfigProperties {
	
	String smtpSender;
	String resetPasswordUrl;
}
