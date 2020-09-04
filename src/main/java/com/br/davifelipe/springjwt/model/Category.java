package com.br.davifelipe.springjwt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude="id")
@EqualsAndHashCode(exclude={"name","products"})
public class Category implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public Category(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    private String name;
	
    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
	private List<Product> products = new ArrayList<>();
	
}
