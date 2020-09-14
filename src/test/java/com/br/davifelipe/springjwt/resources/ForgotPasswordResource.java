package com.br.davifelipe.springjwt.resources;

import static io.restassured.RestAssured.given;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

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
import org.springframework.test.context.TestPropertySource;

import com.br.davifelipe.springjwt.dto.ChangePasswordDTO;
import com.br.davifelipe.springjwt.dto.ForgotPasswordDTO;
import com.br.davifelipe.springjwt.model.ResetPasswordToken;

@TestPropertySource("file:src/test/resources/application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class ForgotPasswordResource extends AbstractApplicationTest{
	
	private ForgotPasswordDTO forgotDTOMock;
	
	private ResetPasswordToken resetPasswordToken;
	
	private ChangePasswordDTO changePasswordDto;
	
	private String usedResetPasswordToken;
	
	
	@BeforeAll
	public void prepare() {
		this.prepareParent();
		
		this.changePasswordDto = new ChangePasswordDTO();
		this.forgotDTOMock = new ForgotPasswordDTO();
	}
	
	@Test
	@DisplayName("Sing Up [POST]")
	@Order(1)
	void singUp() {
		this.singUpParent();
	}
	
	@Test
	@DisplayName("Sing in current password [POST]")
	@Order(2)
	void singInCurrentPassword() {
		this.singInParent();
	}
	
	@Test
	@DisplayName("Forgot password invalid e-mail [POST]")
	@Order(3)
	void forgotPasswordInvalidEmail() {
		
		forgotDTOMock.setEmail("invalid@t");
		
  	     given()
		.contentType("application/json")
		.body(forgotDTOMock)
		.port(port)
		.when().post("/auth/forgot-password")
		.then().statusCode(404);
		
	}
	
	@Test
	@DisplayName("Forgot password request [POST]")
	@Order(4)
	@Transactional()
	void forgotPasswordRequest() {
		
		forgotDTOMock.setEmail(this.userMock.getEmail());
		
  	     given()
		.contentType("application/json")
		.body(forgotDTOMock)
		.port(port)
		.when().post("/auth/forgot-password")
		.then().statusCode(200);
		  	     
  	    this.resetPasswordToken = serviceResetPasswordToken.findByEmail(this.userMock.getEmail());
  	    this.usedResetPasswordToken = this.resetPasswordToken.getToken();
	}
	
	@Transactional
	private void addDayToken(int day) {
		Date createdDate = this.resetPasswordToken.getCreatedDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdDate);
        calendar.add(Calendar.DATE, day);
        Date yesterday = calendar.getTime();
        this.resetPasswordToken.setCreatedDate(yesterday);
        
        repositoryResetPasswordToken.saveAndFlush(this.resetPasswordToken);
	}
	
	@Test
	@DisplayName("Reset invalid password [POST]")
	@Order(5)
	void resetPasswordInvalidPassword() {
		
		this.changePasswordDto.setPassword("newPassword");
		this.changePasswordDto.setPasswordConfirm("newPasswordNotMatch");
		this.changePasswordDto.setToken(this.usedResetPasswordToken);
		
		given()
		.contentType("application/json")
		.body(this.changePasswordDto)
		.port(port)
		.when().post("/auth/reset-password")
		.then().statusCode(400);
	}
	
	@Test
	@DisplayName("Reset expired token [POST]")
	@Order(6)
	void resetExpiredToken() {
		
		//delay one day to simulate an expired token
        this.addDayToken(-1);
        
		ModelMapper modelMapper = new ModelMapper();
		this.changePasswordDto = modelMapper.map(this.resetPasswordToken,ChangePasswordDTO.class);
		
		this.changePasswordDto.setPassword("newPassword");
		this.changePasswordDto.setPasswordConfirm("newPassword");
		
		given()
		.contentType("application/json")
		.body(this.changePasswordDto)
		.port(port)
		.when().post("/auth/reset-password")
		.then().statusCode(400);
	}
	
	@Test
	@DisplayName("Reset password Successfully [POST]")
	@Order(7)
	void resetPasswordSuccessfully() {
		
		this.addDayToken(1);
		
		ModelMapper modelMapper = new ModelMapper();
		this.changePasswordDto = modelMapper.map(this.resetPasswordToken,ChangePasswordDTO.class);
		
		this.changePasswordDto.setPassword("newPassword");
		this.changePasswordDto.setPasswordConfirm("newPassword");
		
		given()
		.contentType("application/json")
		.body(this.changePasswordDto)
		.port(port)
		.when().post("/auth/reset-password")
		.then().statusCode(200);
	}
	
	@Test
	@DisplayName("Checks token reuse exception [POST]")
	@Order(8)
	void tokenReusedExceptionCheck() {
		
		given()
		.contentType("application/json")
		.body(this.changePasswordDto)
		.port(port)
		.when().post("/auth/reset-password")
		.then().statusCode(400);
	}
	
	@Test
	@DisplayName("Check if the new password works [POST]")
	@Order(9)
	void newPasswordCheck() {
		this.singInDTO.setPassword("newPassword");
		this.singInParent();
	}
	
}
