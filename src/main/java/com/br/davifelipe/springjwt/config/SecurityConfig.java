package com.br.davifelipe.springjwt.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.br.davifelipe.springjwt.security.JWTAuthenticationFilter;
import com.br.davifelipe.springjwt.security.JWTAuthorizationFilter;
import com.br.davifelipe.springjwt.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	protected static final List<String> PUBLIC_MATCHERS = new ArrayList<>();
	protected static final List<String> PUBLIC_MATCHERS_GET = new ArrayList<>();
	protected static final List<String> PUBLIC_MATCHERS_POST = new ArrayList<>();
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
    private Environment env;
	
	@Value("${auth.public-sign-up-url-enable}")
	private String publicSignupUrlEnable;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		PUBLIC_MATCHERS.add("/h2-console/**");
		PUBLIC_MATCHERS_GET.add("/product/**");
		PUBLIC_MATCHERS_POST.addAll(Arrays.asList(
										"/auth/reset-password/**",
										"/auth/forgot-password/**"
									));
		
		String[] activeProfiles = env.getActiveProfiles();
		
		List<String> h2Profiles = Arrays.asList("dev", "test");
		
		if(Arrays.asList(activeProfiles).stream().anyMatch(h2Profiles::contains)) {
			//disable it only for h2-console on dev environment
			http.headers().frameOptions().disable();
		}
		
		if("true".equals(this.publicSignupUrlEnable)) {
			PUBLIC_MATCHERS_POST.add("/auth/sign-up/**");
		}
		
		http.cors().and().csrf().disable();
		http.authorizeRequests()
			.antMatchers(PUBLIC_MATCHERS.toArray(new String[0])).permitAll()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST.toArray(new String[0])).permitAll()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET.toArray(new String[0])).permitAll()
			.anyRequest()
			.authenticated();
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bcCryptPasswordEncoder());
	}
	
	@Bean
	public BCryptPasswordEncoder bcCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
