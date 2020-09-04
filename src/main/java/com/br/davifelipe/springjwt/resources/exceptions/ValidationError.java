package com.br.davifelipe.springjwt.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class ValidationError extends StandardError{

	private static final long serialVersionUID = 1L;
	
	@Getter
	private List<FieldMessage> errors = new ArrayList<FieldMessage>();
	
	public ValidationError(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
		// TODO Auto-generated constructor stub
	}

	public void addError(String fieldName, String message) {
		
		this.errors.add(new FieldMessage(fieldName, message));
	}
	
}
