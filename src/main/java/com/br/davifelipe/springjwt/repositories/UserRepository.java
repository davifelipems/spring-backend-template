package com.br.davifelipe.springjwt.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.br.davifelipe.springjwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT user FROM User user JOIN FETCH user.roles roles WHERE user.email = :email")
	public Optional<User> findByEmail(@Param("email") String email);
	
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM User obj WHERE obj.name LIKE %:name%")
	public Page<User> findByName(@Param("name") String name, Pageable pageRequest);
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT user FROM User user WHERE user.email = :email")
	public Page<User> findByEmail(@Param("email") String email, Pageable pageRequest);
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT user FROM User user WHERE user.name LIKE %:name% AND user.email = :email")
	public Page<User> findByNameAndEmail(@Param("name") String name,@Param("email") String email, Pageable pageRequest);
}
