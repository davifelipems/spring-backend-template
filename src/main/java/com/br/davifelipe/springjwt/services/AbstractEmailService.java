package com.br.davifelipe.springjwt.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.br.davifelipe.springjwt.dto.ResetPasswordDTO;
import com.br.davifelipe.springjwt.model.ResetPasswordToken;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${application.smtp-sender}")
	private String smtpSender;
	
	@Value("${application.reset-password-url}")
	private String resetPassowdUrl;
	
	public void sendResetPasswordToken(ResetPasswordToken resetPasswordToken) 
			throws JsonProcessingException {
		SimpleMailMessage msg = this.prepareResetPasswordEmail(resetPasswordToken);
		sendEmail(msg);
	}
	
	protected SimpleMailMessage prepareResetPasswordEmail(ResetPasswordToken resetPasswordToken) 
			throws JsonProcessingException{
		
		SimpleMailMessage sm = new SimpleMailMessage();
		User user = resetPasswordToken.getUser();
		sm.setTo(user.getEmail());
		sm.setFrom(smtpSender);
		sm.setSubject("Reset password");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResetPasswordDTO dto = ObjectMapperUtil.map(resetPasswordToken,ResetPasswordDTO.class);
		
		dto.setPassword("newPassword");
		dto.setPasswordConfirm("newPassword");
		
		StringBuilder sb = new StringBuilder();
		sb.append("I don't have a front end layer yet :( ");
		sb.append("Please access this URL [POST] to reset your password ");
		sb.append(resetPassowdUrl);
		sb.append(" body: ");
		sb.append(objectMapper.writeValueAsString(dto));
		
		sm.setText(sb.toString());
		return sm;
	}
	
}
