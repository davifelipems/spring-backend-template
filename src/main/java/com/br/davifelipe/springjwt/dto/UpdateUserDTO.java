package com.br.davifelipe.springjwt.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.br.davifelipe.springjwt.model.Privilege;
import com.br.davifelipe.springjwt.model.Role;
import com.br.davifelipe.springjwt.services.validation.UpdateUserSave;

import lombok.Data;

@Data
@UpdateUserSave
public class UpdateUserDTO{
	
	private Integer id;
	
	@NotNull
	@Size(min=2, max=30)
	private String name;
	
	@NotNull
	@Email
	private String email;
	
	@Size(min=6)
	private String password;
	
	private List<Role> roles;
	
	private List<Privilege> privileges;
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	public void addPrivilege(Privilege privilege) {
		this.privileges.add(privilege);
	}
	
}
