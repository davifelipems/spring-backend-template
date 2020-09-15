package com.br.davifelipe.springjwt.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.br.davifelipe.springjwt.services.validation.ChagePasswordSave;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@ChagePasswordSave
public class ResetPasswordDTO {
	
	@NotNull
	@Size(min=10)
	private String token;
	
	@NotNull
	@Size(min=6)
	private String password;
	
	@NotNull
	@Size(min=6)
	@JsonProperty("password_confirm")
	private String passwordConfirm;
	
}
