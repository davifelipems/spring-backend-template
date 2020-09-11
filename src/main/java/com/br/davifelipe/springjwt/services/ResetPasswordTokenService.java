package com.br.davifelipe.springjwt.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.davifelipe.springjwt.model.ResetPasswordToken;
import com.br.davifelipe.springjwt.repositories.ResetPasswordTokenRepository;
import com.br.davifelipe.springjwt.services.exceptions.ObjectNotFoundException;

@Service
public class ResetPasswordTokenService {
	
	@Autowired
	private ResetPasswordTokenRepository repo;
	
	/**
	 * Find ResetPasswordToken by token
	 * @param ResetPasswordToken id of the object
	 * @return object found or null if the object were not found
	 * */
	public ResetPasswordToken findByToken(String token) {
		Optional<ResetPasswordToken> obj = this.repo.findByToken(token);
		return obj.orElse(null);
	}
	
	/**
	 * Find ResetPasswordToken by id
	 * @param ResetPasswordToken id of the object
	 * @return object found or null if the object were not found
	 * */
	public ResetPasswordToken findById(Integer id) {
		Optional<ResetPasswordToken> obj = this.repo.findById(id);
		return obj.orElse(null);
	}
	
	/**
	 * Insert a new resetPasswordToken
	 * @param ResetPasswordToken resetPasswordToken to be inserted
	 * @return ResetPasswordToken object inserted 
	 * */
	public ResetPasswordToken insert(ResetPasswordToken obj) {
		obj.setId(null);
		return this.repo.save(obj);
	}
	
	/**
	 * Update an resetPasswordToken
	 * @param resetPasswordToken resetToken to be updated
	 * @return resetPasswordToken object updated 
	 * */
	public ResetPasswordToken update(ResetPasswordToken obj) {
		if(this.findById(obj.getId()) == null) {
			throw new ObjectNotFoundException("Obeject "+ResetPasswordToken.class.getName()+" no found! ID "+obj.getId());
		}
		return this.repo.save(obj);
	}
	
}
