package com.br.davifelipe.springjwt.dto;

import java.util.List;

import com.br.davifelipe.springjwt.services.validation.UserSave;

import lombok.Data;

@Data
@UserSave
public class UserDetailDTO {
	
	private Integer id;
	
	private String name;
	
	private String email;
	
	private List<RoleDTO> roles;
	
	private List<PrivilegeDTO> privileges;
	
}
