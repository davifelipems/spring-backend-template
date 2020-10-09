package com.br.davifelipe.springjwt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude="id")
public class Role implements Serializable{
	
	private static final long serialVersionUID = 1146043919291423157L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
 
    private String name;
    
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "ROLES_PRIVILEGES", 
			joinColumns = @JoinColumn(
					name = "role_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(
					name = "privilege_id", referencedColumnName = "id")) 
	private List<Privilege> privileges = new ArrayList<>();
    
    public void addPrivilege(Privilege privilege) {
    	this.privileges.add(privilege);
    }
    
    public void addUser(User user) {
    	this.users.add(user);
    }
  
}