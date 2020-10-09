package com.br.davifelipe.springjwt.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.br.davifelipe.springjwt.model.Privilege;

import lombok.Data;

@Data
public class RoleSaveDto {
	
	private Integer id;
	
	@NotNull
	@Size(min=2, max=255)
    private String name;
    
    private List<Privilege> privileges = new ArrayList<>();
    
    public void addPrivilege(Privilege privilege) {
    	this.privileges.add(privilege);
    }
}
