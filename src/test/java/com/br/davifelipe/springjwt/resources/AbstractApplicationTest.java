package com.br.davifelipe.springjwt.resources;

import static io.restassured.RestAssured.given;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import com.br.davifelipe.springjwt.dto.SingInDTO;
import com.br.davifelipe.springjwt.dto.SingUpDTO;
import com.br.davifelipe.springjwt.model.User;
import com.br.davifelipe.springjwt.repositories.PrivilegeRepository;
import com.br.davifelipe.springjwt.repositories.ResetPasswordTokenRepository;
import com.br.davifelipe.springjwt.repositories.RoleRepository;
import com.br.davifelipe.springjwt.repositories.UserRepository;
import com.br.davifelipe.springjwt.services.ResetPasswordTokenService;
import com.br.davifelipe.springjwt.services.UserService;
import com.br.davifelipe.springjwt.util.ObjectMapperUtil;

@TestPropertySource("file:src/test/resources/application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
abstract class AbstractApplicationTest {
	
	@LocalServerPort
	protected int port;
	
	protected String token;
	
	protected User userMock;
	
	protected SingInDTO singInDTO;
	
	@Autowired
	protected UserRepository repositoryUser;
	
	@Autowired
	protected RoleRepository repositoryRole;
	
	@Autowired
	protected PrivilegeRepository repositoryPrivilege;
	
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
		
		this.singInDTO = ObjectMapperUtil.map(this.userMock,SingInDTO.class);
		
	}
	
	/**
	 * Checks security layer for unauthorized endponts
	 * */
	protected boolean notAutorizedParent() {
		given()
		.contentType("application/json")
		.port(port)
		.when().get("/category/1")
		.then().statusCode(403);
		
		return true;
	}
	
	/**
	 * Checks sing up endpoint
	 * */
	protected void singUpParent() {
		
		SingUpDTO singUpDTO = ObjectMapperUtil.map(this.userMock,SingUpDTO.class);
		
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
		repositoryPrivilege.deleteAll();
		repositoryRole.deleteAll();
	}

}
