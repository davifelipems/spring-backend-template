package com.br.davifelipe.springjwt.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@ToString(exclude="id")
@EqualsAndHashCode(exclude={"name", "price"})
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter 
	private Integer id;
	
	@Getter
	@Setter	
	private String name;
	
	@Getter
	@Setter
	private BigDecimal price;
	
}
