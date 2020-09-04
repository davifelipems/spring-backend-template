package com.br.davifelipe.springjwt.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude="id")
@EqualsAndHashCode(exclude={"name", "price","categories"})
public class Product implements Serializable{
	
	public Product(Integer id, String name, BigDecimal price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private BigDecimal price;
	
	@ManyToMany
	@JoinTable(name = "PRODUCT_CATEGORY", 
			   joinColumns = @JoinColumn(name = "product_id"),
	           inverseJoinColumns = @JoinColumn(name = "category_id")
			  )
	private List<Category> categories = new ArrayList<>();
	
}
