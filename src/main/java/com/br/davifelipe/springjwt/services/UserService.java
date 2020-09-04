package com.br.davifelipe.springjwt.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserRepository repo;
	
	/**
	 * Find User by id
	 * @param User id of the object
	 * @return object found or null if the object were not found
	 * */
	public User findById(Integer id) {
		Optional<User> obj = this.repo.findById(id);
		return obj.orElse(null);
	}
	
	/**
	 * Insert a new user
	 * @param User user to be inserted
	 * @return User object inserted 
	 * */
	public User insert(User obj) {
		obj.setId(null);
		obj.setPassword(encoder.encode(obj.getPassword()));
		return this.repo.save(obj);
	}
	
}
