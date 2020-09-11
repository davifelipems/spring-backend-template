package com.br.davifelipe.springjwt.services;

import org.springframework.mail.SimpleMailMessage;

import com.br.davifelipe.springjwt.model.ResetPasswordToken;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface EmailService {
	
	void sendEmail(SimpleMailMessage msg);
	
	public void sendResetPasswordToken(ResetPasswordToken resetPasswordToken) throws JsonProcessingException;
}
