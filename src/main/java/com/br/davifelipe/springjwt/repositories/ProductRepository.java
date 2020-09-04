package com.br.davifelipe.springjwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.davifelipe.springjwt.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
}
