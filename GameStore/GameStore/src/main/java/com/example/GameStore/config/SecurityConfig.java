package com.example.GameStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/users").permitAll()
                .antMatchers("/users/**").permitAll() // Bổ sung cho tất cả các yêu cầu liên quan đến người dùng
                .antMatchers("/category").permitAll() // Cho phép tất cả các yêu cầu đến endpoint "/category"
                .antMatchers("/category/**").permitAll() // Bổ sung cho tất cả các yêu cầu liên quan đến danh mục
                .antMatchers("/users/forgot-password").permitAll()
                .antMatchers("/users/{userId}/changePassword").permitAll()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/products").permitAll() // Cho phép tất cả các yêu cầu đến endpoint "/api/products"
                .antMatchers("/products/**").permitAll()
                .anyRequest().authenticated();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
