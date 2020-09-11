package com.br.davifelipe.springjwt.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResetPasswordTokenDTO {
	
	private String token;
	
	@JsonProperty("created_date")
	private Date createdDate;
	
}
