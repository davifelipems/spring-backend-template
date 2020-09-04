package com.br.davifelipe.springjwt.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.davifelipe.springjwt.model.Role;
import com.br.davifelipe.springjwt.repositories.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository repo;
	
	/**
	 * Find Role by id
	 * @param  id of the object
	 * @return object found or null if the object were not found
	 * */
	public Role findById(Integer id) {
		Optional<Role> obj = this.repo.findById(id);
		return obj.orElse(null);
	}
	
	/**
	 * Find Role by name
	 * @param  name of the object
	 * @return object found or null if the object were not found
	 * */
	public Role findByName(String name) {
		Optional<Role> obj = this.repo.findByName(name);
		return obj.orElse(null);
	}
	
	/**
	 * Find or insert a new role if not exists
	 * @param name of role object
	 * @return object found or created
	 * */
	public Role findOrInsertByName(String name) {
		Role newRole = null;
		newRole = this.findByName(name);
		if(newRole == null) {
			newRole = new Role();
			newRole.setId(null);
			newRole.setName(name);
			this.repo.save(newRole);
		}
		return newRole;
	}
}
