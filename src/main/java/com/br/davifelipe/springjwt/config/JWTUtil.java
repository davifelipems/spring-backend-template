package com.br.davifelipe.springjwt.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${auth.jwt-secret}")
	private String jwtSecret;
	
	@Value("${auth.jwt-expiration-miliseg}")
	private Long jwtExpirationMisiseg;
	
	public String genereteToken(String email) {
		return Jwts.builder()
					.setSubject(email)
					.setExpiration(new Date(System.currentTimeMillis() + this.jwtExpirationMisiseg))
					.signWith(SignatureAlgorithm.HS512, this.jwtSecret.getBytes())
					.compact();
	}
}
