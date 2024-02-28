package com.jp.field_connect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.jp.field_connect.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class JdbcAuthConfig {

	@Autowired
	public javax.sql.DataSource dataSource;

	@Autowired
	public UserDetailsServiceImpl userDetailsServ;

	@Bean
	public SecurityFilterChain getFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests().antMatchers("/login","/register/**").permitAll().anyRequest().authenticated();

		http.csrf().disable();
		http.cors().disable();
		http.httpBasic();
		http.formLogin().defaultSuccessUrl("/", false);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsServ);
		return provider;
	}
}
