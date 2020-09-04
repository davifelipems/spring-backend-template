package com.br.davifelipe.springjwt.resources;



import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.br.davifelipe.springjwt.model.Product;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductResourceTest {
	
	@LocalServerPort
	private int port;
	
	@Test
	public void testProduct() {
		
		List<Product> products = Arrays.asList(
												given()
												.contentType("application/json")
												.port(port)
												.when().get("/products/test").then().statusCode(200)
												.extract().as(Product[].class)
											  );
		
		assertEquals("Mouse", products.get(0).getName());

	}
	
}
