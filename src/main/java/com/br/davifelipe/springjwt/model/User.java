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
	
	private static final long serialVersionUID = -6746994790280544901L;
	
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
    private List<Role> roles = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "USERS_PRIVILAGES", 
			joinColumns = @JoinColumn(
					name = "user_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(
					name = "privilege_id", referencedColumnName = "id")) 
	private List<Privilege> privileges = new ArrayList<>();
	
	public Collection<GrantedAuthority> getAutorities() {
		   	
		List<GrantedAuthority> authorities = new ArrayList<>();
        for (Privilege privilege : this.privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }
        
        for (Role role : this.roles) {
        	authorities.add(new SimpleGrantedAuthority(role.getName()));
        	for (Privilege privilege : role.getPrivileges()) {
        		authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        	}
        }
		
		return authorities;
	}
	
	public void addPrivilege(Privilege privilage) {
		this.privileges.add(privilage);
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
}
