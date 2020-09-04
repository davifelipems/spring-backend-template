package com.br.davifelipe.springjwt.resources;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.davifelipe.springjwt.model.Product;

@RestController
@RequestMapping("/products")
public class ProductResoruce {
	
	@GetMapping("/test")
	public Product test() {
		BigDecimal price = new BigDecimal(4.5);
		Product mouse = new Product(1, "Mouse", price);
		
		return mouse;
	}
}
