package com.br.davifelipe.springjwt.services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.br.davifelipe.springjwt.model.ResetPasswordToken;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.repositories.UserRepository;
import com.br.davifelipe.springjwt.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	ResetPasswordTokenService resetPasswordService;
	
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
	 * Find User by e-mail
	 * @param User id of the object
	 * @return object found or null if the object were not found
	 * */
	public User findByEmail(String email) {
		Optional<User> obj = this.repo.findByEmail(email);
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
	
	/**
	 * Update an user
	 * @param User user to be updated
	 * @return User object updated 
	 * */
	public User update(User obj) {
		if(this.findById(obj.getId()) == null) {
			throw new ObjectNotFoundException("Obeject "+User.class.getName()+" no found! ID "+obj.getId());
		}
		obj.setPassword(encoder.encode(obj.getPassword()));
		return this.repo.save(obj);
	}
	
	/**
	 * Generate an reset password token
	 * @param User user that will be password reseted 
	 * @return ResetPasswordToken object whith token genereted
	 * */
	public ResetPasswordToken generateResetPasswordToken(User obj) {
		User user = this.findById(obj.getId());
		ResetPasswordToken resetToken = new ResetPasswordToken();
		
		if(user != null) {
			resetToken.setUser(user);
			Date createdDate = new Date();
			resetToken.setToken(UUID.randomUUID().toString());
			resetToken.setCreatedDate(createdDate);
			resetPasswordService.insert(resetToken);
		}
		return resetToken;
	}
	
}
