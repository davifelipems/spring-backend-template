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

import com.br.davifelipe.springjwt.dto.PrivilegeDTO;
import com.br.davifelipe.springjwt.dto.RoleDTO;
import com.br.davifelipe.springjwt.dto.SignUpDTO;
import com.br.davifelipe.springjwt.dto.UpdateUserDTO;
import com.br.davifelipe.springjwt.dto.UserDTO;
import com.br.davifelipe.springjwt.dto.UserDetailDTO;
import com.br.davifelipe.springjwt.model.Privilege;
import com.br.davifelipe.springjwt.model.Role;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.services.PrivilegeService;
import com.br.davifelipe.springjwt.services.RoleService;
import com.br.davifelipe.springjwt.services.UserService;
import com.br.davifelipe.springjwt.services.exceptions.ObjectNotFoundException;
import com.br.davifelipe.springjwt.util.ObjectMapperUtil;
import com.br.davifelipe.springjwt.util.UrlUtil;

@RestController
@RequestMapping("/user")
public class UserResource {
	
	@Autowired
	private UserService service;
	
	@Autowired
	RoleService serviceRole;
	
	@Autowired
	PrivilegeService servicePrivilege;
	
	@GetMapping(value="/page")
	@PostAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
	public ResponseEntity<Page<UserDTO>> findPage(
			@RequestParam(value="name", defaultValue="") String name, 
			@RequestParam(value="email", defaultValue="") String email, 
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="name") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		
		Page<User> list = null;
		
		if(!name.isEmpty() && !email.isEmpty()) {
			list = service.findPageByNameAndEmail(UrlUtil.decodeParam(name),UrlUtil.decodeParam(email),page, linesPerPage, orderBy, direction);
		}else if(!name.isEmpty()) {
			list = service.findPageByName(UrlUtil.decodeParam(name),page, linesPerPage, orderBy, direction);
		}else if(!email.isEmpty()) {
			list = service.findPageByEmail(UrlUtil.decodeParam(email),page, linesPerPage, orderBy, direction);
		}else {
			list = service.findPage(page, linesPerPage, orderBy, direction);
		}
		
		Page<UserDTO> listDto = ObjectMapperUtil.mapAll(list, UserDTO.class);
		
		return ResponseEntity.ok().body(listDto);
	}
	
	@GetMapping("/{id}")
	@PostAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
	public ResponseEntity<UserDetailDTO> findById(@PathVariable(value="id") Integer id) {
		
		User user = service.findById(id);
		
		if(user == null) {
			throw new ObjectNotFoundException("Object "+User.class.getName()+" not found! id "+id);
		}
		
		UserDetailDTO userDTO = ObjectMapperUtil.map(user,UserDetailDTO.class);
		
		return ResponseEntity.ok().body(userDTO);
	}
	
	@GetMapping("/get-all-privileges")
	@PostAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
	public ResponseEntity<List<PrivilegeDTO>> getAllPrivileges() {
		
		List<PrivilegeDTO> dto = ObjectMapperUtil.mapAll(servicePrivilege.findAll(),PrivilegeDTO.class);
		
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/get-all-roles")
	@PostAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
	public ResponseEntity<List<RoleDTO>> getAllRoles() {
		
		List<RoleDTO> dto = ObjectMapperUtil.mapAll(serviceRole.findAll(),RoleDTO.class);
		
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping()
	@PostAuthorize("hasAuthority('USER_WRITE_PRIVILEGE')")
	public ResponseEntity<Void> insert(@Valid @RequestBody SignUpDTO dto){
		
		User obj = ObjectMapperUtil.map(dto,User.class);
		
		List<Role> rolesUser = new ArrayList<>();
		List<Privilege> privilegesUser = new ArrayList<>();
		
		for (Role role : dto.getRoles()) {
			rolesUser.add(serviceRole.findOrInsertByName(role.getName()));
		}
		
		for (Privilege privilege : dto.getPrivileges()) {
			privilegesUser.add(servicePrivilege.findOrInsertByName(privilege.getName()));
		}
		
		obj.setRoles(rolesUser);
		obj.setPrivileges(privilegesUser);
		obj = this.service.insert(obj);
		URI uri = ServletUriComponentsBuilder
				  .fromCurrentRequest().path("/{id}")
				  .buildAndExpand(obj.getId())
				  .toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	@PostAuthorize("hasAuthority('USER_WRITE_PRIVILEGE')")
	public ResponseEntity<Void> update(@Valid
									   @RequestBody UpdateUserDTO dto,
									   @PathVariable(value="id") Integer id){
		
		User obj = ObjectMapperUtil.map(dto,User.class);
		obj.setId(id);
		
		List<Role> rolesUser = new ArrayList<>();
		List<Privilege> privilegesUser = new ArrayList<>();
		
		for (Role role : dto.getRoles()) {
			rolesUser.add(serviceRole.findOrInsertByName(role.getName()));
		}
		
		for (Privilege privilege : dto.getPrivileges()) {
			privilegesUser.add(servicePrivilege.findOrInsertByName(privilege.getName()));
		}
		
		if(!dto.getRoles().isEmpty()) {
			obj.setRoles(rolesUser);
		}
		
		if(!dto.getPrivileges().isEmpty()){
			obj.setPrivileges(privilegesUser);
		}
		
		this.service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@PostAuthorize("hasAuthority('USER_DELETE_PRIVILEGE')")
	public ResponseEntity<Void> delete(@PathVariable(value="id") Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
