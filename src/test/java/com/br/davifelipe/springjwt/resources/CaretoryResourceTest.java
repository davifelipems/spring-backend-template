package com.br.davifelipe.springjwt.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import com.br.davifelipe.springjwt.dto.CategoryDTO;
import com.br.davifelipe.springjwt.dto.SingInDTO;
import com.br.davifelipe.springjwt.dto.SingUpDTO;
import com.br.davifelipe.springjwt.model.User;

@TestPropertySource("file:src/test/resources/application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class CaretoryResourceTest {
	
	@LocalServerPort
	private int port;
	
	private String token;
	
	private User userMock;
	
	private CategoryDTO categoryDTOMock;
	
	@BeforeAll
	public void prepare() {
		this.userMock = new User();
		this.categoryDTOMock = new CategoryDTO();
		
		this.userMock.setEmail("test@test.com");
		this.userMock.setName("User test");
		this.userMock.setPassword("123456");
		
		this.categoryDTOMock.setName("Devices");
	}
	
	@Test
	@DisplayName("Category not authorized [GET]")
	@Order(1)
	void notAutorized() {
            given()
			.contentType("application/json")
			.port(port)
			.when().get("/category/1")
			.then().statusCode(403);
	}
	
	@Test
	@DisplayName("Sing Up [POST]")
	@Order(2)
	void singUp() {
		
		ModelMapper modelMapper = new ModelMapper();
		SingUpDTO singUpDTO = modelMapper.map(this.userMock,SingUpDTO.class);
		
		given()
		.contentType("application/json")
		.body(singUpDTO)
		.port(port)
		.when().post("/auth/sing-up")
		.then().statusCode(201);
		
	}
	
	@Test
	@DisplayName("Sing in -> Get Token [POST]")
	@Order(3)
	void getToken() {
		
		ModelMapper modelMapper = new ModelMapper();
		SingInDTO singInDTO = modelMapper.map(this.userMock,SingInDTO.class);
		
		this.token =	given()
						.contentType("application/json")
						.body(singInDTO)
						.port(port)
						.when().post("/login")
						.then().statusCode(200).extract().header("Authorization");
		
		
	}
	
	private void categoryNotFound() {
		given()
		.header("Authorization", this.token)
		.contentType("application/json")
		.port(port)
		.when().get("/category/1")
		.then().statusCode(404);
	}
	
	@Test
	@DisplayName("Category no found [GET]")
	@Order(4)
	void notFoundCategory() {
		this.categoryNotFound();
	}
	
	@Test
	@DisplayName("Category insert [POST]")
	@Order(5)
	void insertCategory() {
		
		this.categoryDTOMock.setId(null);
		
		String categorySavedUrl = given()
								.header("Authorization", this.token)
								.contentType("application/json")
								.body(this.categoryDTOMock)
								.port(port)
								.when().post("/category")
								.then().statusCode(201).extract().header("Location");
		
		String splitedUrl[] = categorySavedUrl.split("/");
		this.categoryDTOMock.setId(Integer.parseInt(splitedUrl[splitedUrl.length -1]));
	}
	
	@Test
	@DisplayName("Category found [GET]")
	@Order(6)
	void foundCategory() {
		CategoryDTO caretoryDTORetrived =	given()
				.header("Authorization", this.token)
				.contentType("application/json")
				.port(port)
				.when().get("/category/"+this.categoryDTOMock.getId())
				.then().statusCode(200)
				.extract().as(CategoryDTO.class);

		assertEquals(this.categoryDTOMock.getName(), caretoryDTORetrived.getName());
	}
	
	@Test
	@DisplayName("Category update [PUT]")
	@Order(7)
	void updateCategory() {
		
		this.categoryDTOMock.setName("Device Updated!");
		 given()
		.header("Authorization", this.token)
		.contentType("application/json")
		.body(this.categoryDTOMock)
		.port(port)
		.when().put("/category/"+this.categoryDTOMock.getId())
		.then().statusCode(204);
	}
	
	@Test
	@DisplayName("Category check if it was updated [GET]")
	@Order(8)
	void updateCheckCategory() {
		CategoryDTO caretoryDTORetrived =	given()
									.header("Authorization", this.token)
									.contentType("application/json")
									.port(port)
									.when().get("/category/"+this.categoryDTOMock.getId())
									.then().statusCode(200)
									.extract().as(CategoryDTO.class);
		
		assertEquals(this.categoryDTOMock.getName(), caretoryDTORetrived.getName());
	}
	
	@Test
	@DisplayName("Category delte [DELETE]")
	@Order(9)
	void deleteCategory() {
		
		 given()
		.header("Authorization", this.token)
		.contentType("application/json")
		.port(port)
		.when().delete("/category/"+this.categoryDTOMock.getId())
		.then().statusCode(204);
	}
	
	@Test
	@DisplayName("Category check if it was deleted [GET]")
	@Order(10)
	void delteCheckCategory() {
		this.categoryNotFound();
	}
	
}
