package com.br.davifelipe.springjwt.resources;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.br.davifelipe.springjwt.dto.SingInDTO;
import com.br.davifelipe.springjwt.dto.SignUpDTO;
import com.br.davifelipe.springjwt.dto.UserDTO;
import com.br.davifelipe.springjwt.model.Privilege;
import com.br.davifelipe.springjwt.model.Role;
import com.br.davifelipe.springjwt.services.PrivilegeService;
import com.br.davifelipe.springjwt.services.RoleService;
import com.br.davifelipe.springjwt.util.ObjectMapperUtil;

class UserResourceTest extends AbstractApplicationTest{
	
	@Autowired
	RoleService serviceRole;
	
	@Autowired
	PrivilegeService servicePrivilege;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	private SignUpDTO signupDTO;
	
	private UserDTO userDTO;
	
	@BeforeAll
	void prepare() {
		this.prepareParent();
		
		userMock.setName("User test");
		userMock.setPassword(encoder.encode("123456"));
		Role roleUser = serviceRole.findOrInsertByName("ROLE_USER");
		Privilege caregoryRead = servicePrivilege.findOrInsertByName("USER_READ_PRIVILEGE");
		Privilege caregoryWrite = servicePrivilege.findOrInsertByName("USER_WRITE_PRIVILEGE");
		Privilege caregoryDelete = servicePrivilege.findOrInsertByName("USER_DELETE_PRIVILEGE");
		
		userMock.addRole(roleUser);
		userMock.addPrivilege(caregoryRead);
		userMock.addPrivilege(caregoryWrite);
		userMock.addPrivilege(caregoryDelete);
		
		userMock =	repositoryUser.saveAndFlush(userMock);
		
		this.singInDTO = ObjectMapperUtil.map(this.userMock,SingInDTO.class);
		this.signupDTO = ObjectMapperUtil.map(this.userMock,SignUpDTO.class);
		this.userDTO = ObjectMapperUtil.map(this.userMock,UserDTO.class);
		
		this.signupDTO.setPassword("123456");
		this.singInDTO.setPassword("123456");
	}
	
	@Test
	@DisplayName("User not authorized [GET]")
	@Order(1)
	void notAutorized() {
		assertThat(this.token).isBlank();
		given()
		.contentType("application/json")
		.port(port)
		.when().get("/user/1")
		.then().statusCode(403);
	}
	
	@Test
	@DisplayName("Sign in -> Get Token [POST]")
	@Order(2)
	@Transactional
	void getToken() {
		
		this.token =	given()
				.contentType("application/json")
				.body(singInDTO)
				.port(port)
				.when().post("/login")
				.then().statusCode(200).extract().header("Authorization");
	}
	
	private void userNotFound(Integer id) {
		given()
		.header("Authorization", this.token)
		.contentType("application/json")
		.port(port)
		.when().get("/user/"+id)
		.then().statusCode(404);
	}
	
	@Test
	@DisplayName("User not found [GET]")
	@Order(3)
	void notFoundUser() {
		assertThat(this.token).isNotBlank();
		this.userNotFound(100);
	}
	
	@Test
	@DisplayName("User insert [POST]")
	@Order(4)
	void insertUser() {
		
		assertThat(this.token).isNotBlank();
		
		this.signupDTO.setEmail("test2@test.com");
		
		String userSavedUrl = given()
								.header("Authorization", this.token)
								.contentType("application/json")
								.body(this.signupDTO)
								.port(port)
								.when().post("/user")
								.then().statusCode(201)
								.extract().header("Location");
		
		String splitedUrl[] = userSavedUrl.split("/");
		this.userDTO.setId(Integer.parseInt(splitedUrl[splitedUrl.length -1]));
	}
	
	@Test
	@DisplayName("User found [GET]")
	@Order(5)
	void foundUser() {
		
		UserDTO userDTORetrived =	given()
				.header("Authorization", this.token)
				.contentType("application/json")
				.port(port)
				.when().get("/user/"+this.userDTO.getId())
				.then().statusCode(200)
				.extract().as(UserDTO.class);
		
		assertEquals(this.signupDTO.getName(), userDTORetrived.getName());
	}
	
	@Test
	@DisplayName("User update [PUT]")
	@Order(6)
	void updateUser() {
		
		this.signupDTO.setName("User Updated!");
		 given()
		.header("Authorization", this.token)
		.contentType("application/json")
		.body(this.signupDTO)
		.port(port)
		.when().put("/user/"+this.userDTO.getId())
		.then().statusCode(204);
	}
	
	@Test
	@DisplayName("User check if it was updated [GET]")
	@Order(7)
	void updateCheckUser() {
		UserDTO userDTORetrived =	given()
									.header("Authorization", this.token)
									.contentType("application/json")
									.port(port)
									.when().get("/user/"+this.userDTO.getId())
									.then().statusCode(200)
									.extract().as(UserDTO.class);
		
		assertEquals(this.signupDTO.getName(), userDTORetrived.getName());
	}
	
	@Test
	@DisplayName("User delte [DELETE]")
	@Order(8)
	void deleteUser() {
		
		 given()
		.header("Authorization", this.token)
		.contentType("application/json")
		.port(port)
		.when().delete("/user/"+this.userDTO.getId())
		.then().statusCode(204);
	}
	
	@Test
	@DisplayName("User check if it was deleted [GET]")
	@Order(9)
	void delteCheckUser() {
		this.userNotFound(this.userDTO.getId());
	}
	
	@Test
	@DisplayName("User list [GET]")
	@Order(10)
	void listUser() {
		
		 given()
		.header("Authorization", this.token)
		.contentType("application/json")
		.port(port)
		.when().get("/user/page?email=test@test.com&name=test")
		.then().statusCode(200);
		
	}
	
}
