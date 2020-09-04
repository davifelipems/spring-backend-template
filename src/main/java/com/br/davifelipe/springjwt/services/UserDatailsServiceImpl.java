package com.br.davifelipe.springjwt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.repositories.UserRepository;
import com.br.davifelipe.springjwt.security.MyUserDatails;

@Service
public class UserDatailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repositoryUser;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repositoryUser.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found: "+username);
		}
		
		return new MyUserDatails(user.getId(), user.getEmail(), user.getPassword(), user.getAutorities());
	}


}
