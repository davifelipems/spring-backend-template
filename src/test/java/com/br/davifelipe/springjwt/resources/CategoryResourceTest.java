package com.br.davifelipe.springjwt.resources;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

import com.br.davifelipe.springjwt.dto.CategoryDTO;

class CategoryResourceTest extends AbstractApplicationTest{
	
	@LocalServerPort
	private int port;
	
	private CategoryDTO categoryDTOMock;
	
	@BeforeAll
	void prepare() {
		this.prepareParent();
		
		this.categoryDTOMock = new CategoryDTO();
		this.categoryDTOMock.setName("Devices");
	}
	
	@Test
	@DisplayName("Category not authorized [GET]")
	@Order(1)
	void notAutorized() {
		assertThat(this.token).isBlank();
		this.notAutorizedParent();
	}
	
	@Test
	@DisplayName("Sing Up [POST]")
	@Order(2)
	void singUp() {
		assertThat(this.token).isBlank();
		this.singUpParent();
	}
	
	@Test
	@DisplayName("Sing in -> Get Token [POST]")
	@Order(3)
	@Transactional
	void getToken() {
		assertThat(this.token).isBlank();
		this.singInParent();
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
		assertThat(this.token).isNotBlank();
		this.categoryNotFound();
	}
	
	@Test
	@DisplayName("Category insert [POST]")
	@Order(5)
	void insertCategory() {
		
		this.categoryDTOMock.setId(null);
		assertThat(this.token).isNotBlank();
		
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
