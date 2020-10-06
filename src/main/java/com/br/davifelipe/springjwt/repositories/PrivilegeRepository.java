package com.br.davifelipe.springjwt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.davifelipe.springjwt.model.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer>{
	@Transactional(readOnly = true)
	Optional<Privilege> findByName(@Param("name") String name);
}
