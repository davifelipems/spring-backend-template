package com.br.davifelipe.springjwt.resources;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.br.davifelipe.springjwt.dto.SingUpDTO;
import com.br.davifelipe.springjwt.model.Role;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.services.RoleService;
import com.br.davifelipe.springjwt.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthResource {
	
	@Autowired
	private UserService serviceUser;
	
	@Autowired
	private RoleService serviceRole;
	
	@PostMapping("/sing-up")
	public ResponseEntity<Void> singUp(@Valid @RequestBody SingUpDTO dto){
		
		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(dto,User.class);
		
		Role roleUser = serviceRole.findOrInsertByName("ROLE_USER");
		
		user.addRole(roleUser);
		user = this.serviceUser.insert(user);
		
		URI uri = ServletUriComponentsBuilder
				  .fromCurrentContextPath().path("/{id}")
				  .buildAndExpand(user.getId())
				  .toUri();
		return ResponseEntity.created(uri).build();
	}
	
}
