package com.br.davifelipe.springjwt.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.davifelipe.springjwt.model.Privilege;
import com.br.davifelipe.springjwt.repositories.PrivilegeRepository;

@Service
public class PrivilegeService {
	
	@Autowired
	private PrivilegeRepository repo;
	
	/**
	 * Find Privilege by id
	 * @param  id of the object
	 * @return object found or null if the object were not found
	 * */
	public Privilege findById(Integer id) {
		Optional<Privilege> obj = this.repo.findById(id);
		return obj.orElse(null);
	}
	
	/**
	 * Find Privilege by name
	 * @param  name of the object
	 * @return object found or null if the object were not found
	 * */
	public Privilege findByName(String name) {
		Optional<Privilege> obj = this.repo.findByName(name);
		return obj.orElse(null);
	}
	
	/**
	 * Find or insert a new privilege if not exists
	 * @param name of privilege object
	 * @return object found or created
	 * */
	public Privilege findOrInsertByName(String name) {
		Privilege newObject = null;
		newObject = this.findByName(name);
		if(newObject == null) {
			newObject = new Privilege();
			newObject.setId(null);
			newObject.setName(name);
			this.repo.save(newObject);
		}
		return newObject;
	}
}
