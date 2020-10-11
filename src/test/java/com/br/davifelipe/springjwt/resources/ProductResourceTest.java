package com.br.davifelipe.springjwt.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.davifelipe.springjwt.dto.ProductDTO;
import com.br.davifelipe.springjwt.model.Product;
import com.br.davifelipe.springjwt.repositories.ProductRepository;

class ProductResourceTest extends AbstractApplicationTest{
	
	@Autowired
	private ProductRepository repositoryProduct;
	
	private Product insertedProduct;
	
	@BeforeAll
	void prepare() {
		
		this.insertedProduct = new Product();
		this.insertedProduct.setName("Mouse");
		this.insertedProduct.setPrice(new BigDecimal(4.5));
		this.insertedProduct = this.repositoryProduct.saveAndFlush(this.insertedProduct);
	}
	
	@Test
	@DisplayName("Get product by ID")
	void getProductById() {
		
		ProductDTO productDTO = given()
						  .contentType("application/json")
						  .port(port)
						  .when().get("/product/"+this.insertedProduct.getId())
						  .then().statusCode(200)
						  .extract().as(ProductDTO.class);
		
		assertEquals(this.insertedProduct.getName(), productDTO.getName());

	}
	
}
