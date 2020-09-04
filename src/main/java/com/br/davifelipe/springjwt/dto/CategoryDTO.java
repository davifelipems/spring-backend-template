package com.br.davifelipe.springjwt.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CategoryDTO {
	
	Integer id;
	
	@NotNull
	@Size(min=2, max=30)
	String name;
}
