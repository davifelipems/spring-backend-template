package com.br.davifelipe.springjwt.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.davifelipe.springjwt.model.Product;
import com.br.davifelipe.springjwt.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductResoruce {
	
	@Autowired
	private ProductService service;
	
	@GetMapping("/find-by-id/{id}")
	public ResponseEntity<?> findById(@PathVariable(value="id") String id) {
		
		Product product = service.findByid(Integer.parseInt(id));
		
		return ResponseEntity.ok().body(product);
	}
	
	@GetMapping("/test")
	public List<Product> test() {
		
		BigDecimal price = new BigDecimal(4.5);
		Product mouse = new Product(1, "Mouse", price);
		
		List<Product> lista = new ArrayList<Product>();
		lista.add(mouse);
		
		return lista;
	}
}
