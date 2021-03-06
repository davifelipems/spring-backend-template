package com.br.davifelipe.springjwt.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.davifelipe.springjwt.dto.ProductDTO;
import com.br.davifelipe.springjwt.model.Product;
import com.br.davifelipe.springjwt.services.ProductService;
import com.br.davifelipe.springjwt.services.exceptions.ObjectNotFoundException;
import com.br.davifelipe.springjwt.util.ObjectMapperUtil;

@RestController
@RequestMapping("/product")
public class ProductResoruce {
	
	@Autowired
	private ProductService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable(value="id") String id) {
		
		Product product = service.findByid(Integer.parseInt(id));
		
		if(product == null) {
			throw new ObjectNotFoundException("Object Product not found! id "+id);
		}
		
		ProductDTO productDTO = ObjectMapperUtil.map(product,ProductDTO.class);
		
		return ResponseEntity.ok().body(productDTO);
	}
	
}
