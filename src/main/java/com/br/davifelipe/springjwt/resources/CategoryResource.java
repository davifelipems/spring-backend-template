package com.br.davifelipe.springjwt.resources;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.br.davifelipe.springjwt.dto.CategoryDTO;
import com.br.davifelipe.springjwt.model.Category;
import com.br.davifelipe.springjwt.services.CategoryService;
import com.br.davifelipe.springjwt.services.exceptions.ObjectNotFoundException;
import com.br.davifelipe.springjwt.util.ObjectMapperUtil;

@RestController
@RequestMapping("/category")
public class CategoryResource {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping(value="/page")
	@PostAuthorize("hasAuthority('CATEGORY_READ_PRIVILEGE')")
	public ResponseEntity<Page<CategoryDTO>> findPage(
			@RequestParam(value="name", defaultValue="") String name, 
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="name") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		
		Page<Category> list = null;
		
		list = service.findPage(page, linesPerPage, orderBy, direction);
		
		Page<CategoryDTO> listDto = ObjectMapperUtil.mapAll(list, CategoryDTO.class);
		
		return ResponseEntity.ok().body(listDto);
	}
	
	@GetMapping("/{id}")
	@PostAuthorize("hasAuthority('CATEGORY_READ_PRIVILEGE')")
	public ResponseEntity<CategoryDTO> findById(@PathVariable(value="id") Integer id) {
		
		ModelMapper modelMapper = new ModelMapper();
		Category category = service.findById(id);
		
		if(category == null) {
			throw new ObjectNotFoundException("Object "+Category.class.getName()+" not found! id "+id);
		}
		
		CategoryDTO categoryDTO = modelMapper.map(category,CategoryDTO.class);
		return ResponseEntity.ok().body(categoryDTO);
	}
	
	@PostMapping()
	@PostAuthorize("hasAuthority('CATEGORY_WRITE_PRIVILEGE')")
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoryDTO dto){
		
		ModelMapper modelMapper = new ModelMapper();
		Category obj = modelMapper.map(dto,Category.class);
		
		obj = this.service.insert(obj);
		URI uri = ServletUriComponentsBuilder
				  .fromCurrentRequest().path("/{id}")
				  .buildAndExpand(obj.getId())
				  .toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	@PostAuthorize("hasAuthority('CATEGORY_WRITE_PRIVILEGE')")
	public ResponseEntity<Void> update(@Valid
									   @RequestBody CategoryDTO dto,
									   @PathVariable(value="id") Integer id){
		
		ModelMapper modelMapper = new ModelMapper();
		Category obj = modelMapper.map(dto,Category.class);
		obj.setId(id);
		this.service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@PostAuthorize("hasAuthority('CATEGORY_DELETE_PRIVILEGE')")
	public ResponseEntity<Void> delete(@PathVariable(value="id") Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
