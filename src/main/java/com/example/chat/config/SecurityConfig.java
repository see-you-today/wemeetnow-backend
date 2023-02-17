package com.example.chat.config;

import com.example.chat.config.entrypoint.CustomAuthenticationEntryPoint;
import com.example.chat.config.jwt.JwtFilter;
import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private static final String[] PERMIT_URL = {
        "/api/v1/users/join", "/api/v1/users/login", "/api/v1/chat/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_URL).permitAll()
                .antMatchers("/api/v1/users").hasRole("ADMIN")
                .antMatchers(POST, "/api/v1/**").authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
//                .oauth2Login()
//                .userInfoEndpoint()
                .addFilterBefore(new JwtFilter(userService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
