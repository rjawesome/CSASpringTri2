package com.nighthawk.spring_portfolio.security;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nighthawk.spring_portfolio.mvc.flashcards.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.login.JwtAuthenticationEntryPoint;
import com.nighthawk.spring_portfolio.mvc.login.JwtRequestFilter;

/*
* To enable HTTP Security in Spring, extend the WebSecurityConfigurerAdapter. 
*/

@Configuration
@EnableWebSecurity  // Beans to enable basic Web security
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private PersonDetailsService personDetailsService;

    @Value("${jwt.random-secret}")
    static String salt;

    // Provide a default configuration using configure(HttpSecurity http)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                // .antMatchers("/mvc/person/update/**", "/mvc/person/delete/**").authenticated()
				// .antMatchers("/api/person/**").authenticated()
                .antMatchers("/api/jwt/**").permitAll()
				.and()
            .cors().and()
            .headers()
                    .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Credentials", "true"))
                    .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-ExposedHeaders", "*", "Authorization"))
                    .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "Content-Type", "Authorization", "x-csrf-token"))
                    .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-MaxAge", "600"))
                    .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "POST", "GET", "OPTIONS", "HEAD"))
                    //.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "https://csa.rohanj.dev"))
                    .and()
                .formLogin()
            .loginPage("/login")
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
				.and()
			// make sure we use stateless session; 
			// session won't be used to store user's state.
			.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // Add a filter to validate the tokens with every request
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                    // Cross-Site Request Forgery disable for JS Fetch URIs

    }

    // Should fix the JWT api controller
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean  // Sets up password encoding style
    PasswordEncoder passwordEncoder(){
        //return new StandardPasswordEncoder();
        return new BCryptPasswordEncoder();
    }

    public static String bcryptencode(String password) {
        Charset charset = StandardCharsets.US_ASCII;
        byte[] byteSalt = charset.encode("salt").array();

         SecureRandom random = new SecureRandom(byteSalt);

        return new BCryptPasswordEncoder(BCryptVersion.$2Y, random).encode(password);
    }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(personDetailsService).passwordEncoder(passwordEncoder());
	}



}
