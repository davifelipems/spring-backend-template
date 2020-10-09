package com.br.davifelipe.springjwt.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.davifelipe.springjwt.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	@Transactional(readOnly = true)
	Optional<Role> findByName(@Param("name") String name);
	
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Role obj WHERE obj.name LIKE %:name%")
	public Page<Role> findByName(@Param("name") String name, Pageable pageRequest);
}
