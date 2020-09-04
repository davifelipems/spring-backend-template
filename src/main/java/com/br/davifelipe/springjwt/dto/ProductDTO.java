package com.br.davifelipe.springjwt.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductDTO {
	
	private Integer id;
	
	private String name;
	
	private BigDecimal price;
}
