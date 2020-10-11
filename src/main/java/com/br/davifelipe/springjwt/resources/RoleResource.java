package com.br.davifelipe.springjwt.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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

import com.br.davifelipe.springjwt.dto.RoleDto;
import com.br.davifelipe.springjwt.dto.RoleSaveDto;
import com.br.davifelipe.springjwt.model.Privilege;
import com.br.davifelipe.springjwt.model.Role;
import com.br.davifelipe.springjwt.services.PrivilegeService;
import com.br.davifelipe.springjwt.services.RoleService;
import com.br.davifelipe.springjwt.services.exceptions.ObjectNotFoundException;
import com.br.davifelipe.springjwt.util.ObjectMapperUtil;
import com.br.davifelipe.springjwt.util.UrlUtil;

@RestController
@RequestMapping("/role")
public class RoleResource {
	
	@Autowired
	RoleService serviceRole;
	
	@Autowired
	PrivilegeService servicePrivilege;
	
	@GetMapping(value="/page")
	public ResponseEntity<Page<RoleDto>> findPage(
			@RequestParam(value="name", defaultValue="") String name, 
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="name") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		
		Page<Role> list = null;
		
		if(!name.isEmpty()) {
			list = serviceRole.findPageByName(UrlUtil.decodeParam(name),page, linesPerPage, orderBy, direction);
		}else {
			list = serviceRole.findPage(page, linesPerPage, orderBy, direction);
		}
		
		Page<RoleDto> listDto = ObjectMapperUtil.mapAll(list, RoleDto.class);
		
		return ResponseEntity.ok().body(listDto);
	}
	
	@GetMapping("/{id}")
	@PostAuthorize("hasAuthority('ROLE_READ_PRIVILEGE')")
	public ResponseEntity<RoleDto> findById(@PathVariable(value="id") Integer id) {
		
		Role role = serviceRole.findById(id);
		
		if(role == null) {
			throw new ObjectNotFoundException("Object "+Role.class.getName()+" not found! id "+id);
		}
		
		RoleDto roleDTO = ObjectMapperUtil.map(role,RoleDto.class);
		return ResponseEntity.ok().body(roleDTO);
	}
	
	@PostMapping()
	@PostAuthorize("hasAuthority('ROLE_WRITE_PRIVILEGE')")
	public ResponseEntity<Void> insert(@Valid @RequestBody RoleSaveDto dto){
		
		Role obj = ObjectMapperUtil.map(dto,Role.class);
		
		List<Privilege> privilegesUser = new ArrayList<>();
		
		for (Privilege privilege : dto.getPrivileges()) {
			privilegesUser.add(servicePrivilege.findOrInsertByName(privilege.getName()));
		}
		
		obj.setPrivileges(privilegesUser);
		
		obj = this.serviceRole.insert(obj);
		URI uri = ServletUriComponentsBuilder
				  .fromCurrentRequest().path("/{id}")
				  .buildAndExpand(obj.getId())
				  .toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	@PostAuthorize("hasAuthority('ROLE_WRITE_PRIVILEGE')")
	public ResponseEntity<Void> update(@Valid
									   @RequestBody RoleSaveDto dto,
									   @PathVariable(value="id") Integer id){
		
		Role obj = ObjectMapperUtil.map(dto,Role.class);
		obj.setId(id);
		
		List<Privilege> privilegesUser = new ArrayList<>();
		
		for (Privilege privilege : dto.getPrivileges()) {
			privilegesUser.add(servicePrivilege.findOrInsertByName(privilege.getName()));
		}
		
		obj.setPrivileges(privilegesUser);
		
		this.serviceRole.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@PostAuthorize("hasAuthority('ROLE_DELETE_PRIVILEGE')")
	public ResponseEntity<Void> delete(@PathVariable(value="id") Integer id) {
		serviceRole.delete(id);
		return ResponseEntity.noContent().build();
	}
}
