package com.br.davifelipe.springjwt.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Emulating e-mail sending...");
		LOG.info(msg.toString());
		LOG.info("e-mail sent");
	}

}
