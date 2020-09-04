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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.br.davifelipe.springjwt.security.JWTAuthenticationFilter;
import com.br.davifelipe.springjwt.security.JWTAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	public static final List<String> PUBLIC_MATCHERS = new ArrayList<String>();
	public static final List<String> PUBLIC_MATCHERS_GET = new ArrayList<String>();
	public static final List<String> PUBLIC_MATCHERS_POST = new ArrayList<String>();
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
    private Environment env;
	
	@Value("${auth.public-sing-up-url-enable}")
	private String publicSingUpUrlEnable;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		PUBLIC_MATCHERS.add("/h2-console/**");
		PUBLIC_MATCHERS_GET.add("/products/**");
		
		String[] activeProfiles = env.getActiveProfiles();
		
		if (Arrays.asList(activeProfiles).contains("dev")) {
			//disable it only for h2-console on dev envioment
			http.headers().frameOptions().disable();
		}
		
		if("true".equals(this.publicSingUpUrlEnable)) {
			PUBLIC_MATCHERS_POST.add("/auth/sing-up/**");
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
	CorsConfigurationSource configurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bcCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
