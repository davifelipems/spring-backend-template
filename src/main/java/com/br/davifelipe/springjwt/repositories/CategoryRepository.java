package com.br.davifelipe.springjwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.davifelipe.springjwt.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
