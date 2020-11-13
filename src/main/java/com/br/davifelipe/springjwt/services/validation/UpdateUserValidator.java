package com.br.davifelipe.springjwt.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.davifelipe.springjwt.dto.UpdateUserDTO;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.resources.exceptions.FieldMessage;
import com.br.davifelipe.springjwt.services.UserService;

public class UpdateUserValidator implements ConstraintValidator<UpdateUserSave, UpdateUserDTO> {
	
	@Autowired
	private UserService userService;
	
	@Override
	public boolean isValid(UpdateUserDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> erros = new ArrayList<>();
		
		//Make custom validations here and add that in que erros list 
		if(objDto.getEmail() == null) {
			erros.add(new FieldMessage("email","can not be null"));
		}
		
		User userFound = userService.findByEmail(objDto.getEmail());
		
		if(userFound != null && !userFound.getId().equals(objDto.getId())) {
			erros.add(new FieldMessage("email","User already registered!"));
		}
		
		for (FieldMessage e : erros) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return erros.isEmpty();
	}
}
