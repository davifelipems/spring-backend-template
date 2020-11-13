package com.br.davifelipe.springjwt.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.br.davifelipe.springjwt.model.Role;
import com.br.davifelipe.springjwt.repositories.RoleRepository;
import com.br.davifelipe.springjwt.services.exceptions.ObjectNotFoundException;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository repo;
	
	public Page<Role> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Page<Role> findPageByName(String name,Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findByName(name, pageRequest);
	}
	
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
	
	/**
	 * Insert a new role
	 * @param Role role to be inserted
	 * @return Role object inserted 
	 * */
	public Role insert(Role obj) {
		obj.setId(null);
		return this.repo.save(obj);
	}
	
	/**
	 * Update an role
	 * @param Role role to be updated
	 * @return Role object updated 
	 * */
	public Role update(Role obj) {
		if(this.findById(obj.getId()) == null) {
			throw new ObjectNotFoundException("Obeject "+Role.class.getName()+" no found! ID "+obj.getId());
		}
		return this.repo.save(obj);
	}
	
	/**
	 * Delete a role object by id
	 * @param role id
	 * */
	public void delete(Integer id) {
		if(this.findById(id) == null) {
			throw new ObjectNotFoundException("Obeject "+Role.class.getName()+" no found! ID "+id);
		}
		repo.deleteById(id);
	}
	
	public List<Role> findAll(){
		return repo.findAll();
	}
}
