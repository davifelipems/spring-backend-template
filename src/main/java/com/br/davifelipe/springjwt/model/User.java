package com.br.davifelipe.springjwt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude="id")
@EqualsAndHashCode(exclude={"name","email","password"})
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String email;
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( 
        name = "USERS_ROLES", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private List<Role> roles = new ArrayList<Role>();
	
	public Collection<? extends GrantedAuthority> getAutorities() {
		
		Collection<? extends GrantedAuthority> authorities = this.getRoles()
				 .stream()
				 .map(r -> new SimpleGrantedAuthority(r.getName()))
				 .collect(Collectors.toList());
		return authorities;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
}
