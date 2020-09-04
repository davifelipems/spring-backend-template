package com.br.davifelipe.springjwt.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

@Entity
@Data
public class Role {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
 
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new ArrayList<User>();
    
    public void addUser(User user) {
    	this.users.add(user);
    }
  
}