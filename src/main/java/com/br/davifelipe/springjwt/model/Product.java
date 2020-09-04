package com.br.davifelipe.springjwt.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor @NoArgsConstructor
@ToString(exclude="id")
@EqualsAndHashCode(exclude={"name", "price"})
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Getter private int id;
	@Getter @Setter	private String name;
	@Getter @Setter private BigDecimal price;
	
}
