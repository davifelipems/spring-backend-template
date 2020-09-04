package com.br.davifelipe.springjwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.davifelipe.springjwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
