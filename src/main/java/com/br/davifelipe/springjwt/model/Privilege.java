package com.br.davifelipe.springjwt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude="id")
public class Privilege implements Serializable{

	private static final long serialVersionUID = 8817356608384481025L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	private String name;
	
	@ManyToMany(mappedBy = "privileges")
	private Collection<User> users = new ArrayList<>();
	
	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles = new ArrayList<>();
}
