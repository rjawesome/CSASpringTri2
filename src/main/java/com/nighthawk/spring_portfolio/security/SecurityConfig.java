package com.nighthawk.spring_portfolio.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
* To enable HTTP Security in Spring, extend the WebSecurityConfigurerAdapter. 
*/
@EnableWebSecurity  // Beans to enable basic Web security
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Provide a default configuration using configure(HttpSecurity http)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();  // Cross-Site Request Forgery disable for JS Fetch URIs
    }

    // Should fix the JWT api controller
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
