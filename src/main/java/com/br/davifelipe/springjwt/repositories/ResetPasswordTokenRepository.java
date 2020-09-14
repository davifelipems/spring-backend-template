package com.br.davifelipe.springjwt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.davifelipe.springjwt.model.ResetPasswordToken;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer>{
	
	@Transactional(readOnly = true)
	public Optional<ResetPasswordToken> findByToken(@Param("token") String token);
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT spt FROM ResetPasswordToken spt JOIN spt.user user WHERE user.email = :email")
	public Optional<ResetPasswordToken> findByEmail(@Param("email") String email);
}
