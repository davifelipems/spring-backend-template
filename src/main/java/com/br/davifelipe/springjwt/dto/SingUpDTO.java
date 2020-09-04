package com.br.davifelipe.springjwt.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SingUpDTO {
	
	@NotNull
	@Size(min=2, max=30)
	private String name;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	@Size(min=2)
	private String password;
}
