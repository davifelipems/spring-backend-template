package com.br.davifelipe.springjwt.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoleDetailDTO {
	
	private Integer id;
	 
    private String name;
    
    private List<PrivilegeDTO> privileges;
}
