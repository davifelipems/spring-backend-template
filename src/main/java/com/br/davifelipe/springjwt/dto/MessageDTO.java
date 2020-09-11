package com.br.davifelipe.springjwt.dto;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.Data;

@Data
public class MessageDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
	private void setValues(String message,String error,Integer status) {
		URI uri = ServletUriComponentsBuilder
				  .fromCurrentRequest().build().toUri();
		
		this.status = status;
		this.timestamp = new Date().getTime();
		this.message = message;
		this.path = uri.getRawPath();
		this.error = error;
	}
	
	public MessageDTO(String message,String error,Integer status) {
		this.setValues(message, error, status);
	}
	
	public MessageDTO(String message,Integer status) {
		this.setValues(message, null, status);
	}	
}
