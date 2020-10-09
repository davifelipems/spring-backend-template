package com.br.davifelipe.springjwt.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.br.davifelipe.springjwt.dto.ForgotPasswordDTO;
import com.br.davifelipe.springjwt.dto.MessageDTO;
import com.br.davifelipe.springjwt.dto.ResetPasswordDTO;
import com.br.davifelipe.springjwt.dto.SingUpDTO;
import com.br.davifelipe.springjwt.model.Privilege;
import com.br.davifelipe.springjwt.model.ResetPasswordToken;
import com.br.davifelipe.springjwt.model.Role;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.services.EmailService;
import com.br.davifelipe.springjwt.services.PrivilegeService;
import com.br.davifelipe.springjwt.services.ResetPasswordTokenService;
import com.br.davifelipe.springjwt.services.RoleService;
import com.br.davifelipe.springjwt.services.UserService;
import com.br.davifelipe.springjwt.services.exceptions.ObjectNotFoundException;
import com.br.davifelipe.springjwt.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/auth")
public class AuthResource {
	
	@Autowired
	private UserService serviceUser;
	
	@Autowired
	private RoleService serviceRole;
	
	@Autowired
	private PrivilegeService servicePrivilege;
	
	@Autowired
	private ResetPasswordTokenService serviceResetPassword;
	
	@Autowired
	EmailService emailService;
	
	@Value("${auth.reset-password-token-expiration-miliseg}")
	private Long resetPasswordTokenExpirationMisiseg;
	
	@PostMapping("/sing-up")
	public ResponseEntity<Void> singUp(@Valid @RequestBody SingUpDTO dto){
		
		User user = ObjectMapperUtil.map(dto,User.class);
		
		Role roleUser = serviceRole.findOrInsertByName("ROLE_USER");
		Privilege caregoryRead = servicePrivilege.findOrInsertByName("CATEGORY_READ_PRIVILEGE");
		Privilege caregoryWrite = servicePrivilege.findOrInsertByName("CATEGORY_WRITE_PRIVILEGE");
		Privilege caregoryDelete = servicePrivilege.findOrInsertByName("CATEGORY_DELETE_PRIVILEGE");
		
		user.addRole(roleUser);
		user.addPrivilege(caregoryRead);
		user.addPrivilege(caregoryWrite);
		user.addPrivilege(caregoryDelete);
		
		user = this.serviceUser.insert(user);
		
		URI uri = ServletUriComponentsBuilder
				  .fromCurrentContextPath().path("user/{id}")
				  .buildAndExpand(user.getId())
				  .toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<MessageDTO> findById(@Valid @RequestBody ResetPasswordDTO dto) {
		
		ResetPasswordToken resetPasswordToken = serviceResetPassword.findByToken(dto.getToken());
		if(resetPasswordToken == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
					new MessageDTO("Invalid link","Reset password error", HttpStatus.BAD_REQUEST.value())
					);
		}
		
		if(resetPasswordToken.isExpired(resetPasswordTokenExpirationMisiseg)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
					new MessageDTO("Expired link","Reset password error", HttpStatus.BAD_REQUEST.value())
					);
		}
		
		User user = resetPasswordToken.getUser();
		user.setPassword(dto.getPassword());
		serviceUser.update(user);
		//clean token to prevent that it will be used more than once
		resetPasswordToken.setToken(null);
		serviceResetPassword.update(resetPasswordToken);
		
		return ResponseEntity.ok().body(new MessageDTO("Password was changed", HttpStatus.OK.value()));
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<MessageDTO> findById(@Valid @RequestBody ForgotPasswordDTO dto) 
			throws JsonProcessingException {
		
		User userFound = serviceUser.findByEmail(dto.getEmail());
		
		if(userFound == null) {
			throw new ObjectNotFoundException("Object "+User.class.getName()+" not found! e-mail "+dto.getEmail());
		}
		
		ResetPasswordToken resetPasswordToken = serviceUser.generateResetPasswordToken(userFound);
		emailService.sendResetPasswordToken(resetPasswordToken);
		
		StringBuilder sb = new StringBuilder();
		sb.append("An e-mail has been sent to the addres you have provided.");
		sb.append("Please follow the link in the e-mail to complete you password reset request.");
		
		return ResponseEntity.ok().body(new MessageDTO(sb.toString(),
														HttpStatus.OK.value()
														)
										);
	}
	
}
