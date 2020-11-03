package com.br.davifelipe.springjwt.dto;

import com.br.davifelipe.springjwt.services.validation.UserSave;

import lombok.Data;

@Data
@UserSave
public class UserDTO {
	
	private Integer id;
	
	private String name;
	
	private String email;
	
}
