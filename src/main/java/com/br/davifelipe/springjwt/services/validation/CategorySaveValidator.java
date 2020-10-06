package com.br.davifelipe.springjwt.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.br.davifelipe.springjwt.dto.CategoryDTO;
import com.br.davifelipe.springjwt.resources.exceptions.FieldMessage;

public class CategorySaveValidator implements ConstraintValidator<CategorySave, CategoryDTO> {
	
	@Override
	public boolean isValid(CategoryDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> erros = new ArrayList<>();
		
		//Make custom validations here and add that in que erros list 
		if(objDto.getName().equals("null")) {
			erros.add(new FieldMessage("name","can not be String null"));
		}
		
		for (FieldMessage e : erros) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return erros.isEmpty();
	}
}
