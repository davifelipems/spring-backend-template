package com.br.davifelipe.springjwt.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.davifelipe.springjwt.model.Product;
import com.br.davifelipe.springjwt.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repo;
	
	/**
	 * Find a Product object by id
	 * @param Integer id of the object
	 * @return object found or null if the object were not found
	 * */
	public Product findByid(Integer id) {
		Optional<Product> obj = repo.findById(id);
		return obj.orElse(null);
	}
}
