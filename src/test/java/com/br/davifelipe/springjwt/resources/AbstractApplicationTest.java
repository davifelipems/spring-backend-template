package com.br.davifelipe.springjwt.resources;

import static io.restassured.RestAssured.given;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import com.br.davifelipe.springjwt.dto.SingInDTO;
import com.br.davifelipe.springjwt.dto.SingUpDTO;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.repositories.ResetPasswordTokenRepository;
import com.br.davifelipe.springjwt.repositories.UserRepository;
import com.br.davifelipe.springjwt.services.ResetPasswordTokenService;
import com.br.davifelipe.springjwt.services.UserService;

abstract class AbstractApplicationTest {
	
	@LocalServerPort
	protected int port;
	
	protected String token;
	
	protected User userMock;
	
	protected SingInDTO singInDTO;
	
	@Autowired
	protected UserRepository repositoryUser;
	
	@Autowired
	protected UserService serviceUser;
	
	@Autowired
	protected ResetPasswordTokenService serviceResetPasswordToken;
	
	@Autowired
	protected ResetPasswordTokenRepository repositoryResetPasswordToken;
	
	/**
	 * Prepare objects to be used in other methods
	 * */
	protected void prepareParent() {
		
		this.clearData();
		this.userMock = new User();
		
		this.userMock.setEmail("test@test.com");
		this.userMock.setName("User test");
		this.userMock.setPassword("123456");
		
		ModelMapper modelMapper = new ModelMapper();
		this.singInDTO = modelMapper.map(this.userMock,SingInDTO.class);
		
	}
	
	/**
	 * Checks security layer for unauthorized endponts
	 * */
	protected void notAutorizedParent() {
		given()
		.contentType("application/json")
		.port(port)
		.when().get("/category/1")
		.then().statusCode(403);
	}
	
	/**
	 * Checks sing up endpoint
	 * */
	protected void singUpParent() {
		ModelMapper modelMapper = new ModelMapper();
		SingUpDTO singUpDTO = modelMapper.map(this.userMock,SingUpDTO.class);
		
		given()
		.contentType("application/json")
		.body(singUpDTO)
		.port(port)
		.when().post("/auth/sing-up")
		.then().statusCode(201);
	}
	
	/**
	 * Checks sing in endpoint
	 * */
	protected void singInParent() {
		
		this.token =	given()
						.contentType("application/json")
						.body(singInDTO)
						.port(port)
						.when().post("/login")
						.then().statusCode(200).extract().header("Authorization");
	}
	
	@Transactional
	private void clearData() {
		repositoryResetPasswordToken.deleteAll();
		repositoryUser.deleteAll();
	}

}
