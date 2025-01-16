package com.zeus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("SecurityConfig");

		// csrf 토큰 비활성화
		http.csrf().disable();
		// URI 패턴으로 모든 접근 제한을 설정한다.
		http.authorizeRequests().requestMatchers("/board/list").permitAll();
		// 로그인사람만 가능
		http.authorizeRequests().requestMatchers("/board/register").hasRole("MEMBER");;
		// 로그인하고 멤버 인가받은사람만 가능
		http.authorizeRequests().requestMatchers("/notice/list").permitAll();
		// 로그인하고 관리자로 인가받은사람만 가능
		http.authorizeRequests().requestMatchers("/notice/register").hasRole("ADMIN");
		// 접근 거부 처리자의 URI를 지정
		http.exceptionHandling().accessDeniedPage("/accessError");
		// 폼 기반 인증기능을 사용한다.
		http.formLogin();
		return http.build();

	}

	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 지정된 아이디와 패스워드로 로그인이 가능하도록 설정한다.
		auth.inMemoryAuthentication().withUser("member").password("{noop}1234").roles("MEMBER");
		auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("ADMIN");
	}

}