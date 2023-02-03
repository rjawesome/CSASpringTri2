package com.nighthawk.spring_portfolio.mvc.login;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nighthawk.spring_portfolio.mvc.flashcards.Person;
import com.nighthawk.spring_portfolio.mvc.flashcards.PersonDetailsService;

public class JwtApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private PersonDetailsService personDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Person authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPasswordHash());
		final UserDetails userDetails = personDetailsService // Don't worry I'll fix this later
				.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final ResponseCookie tokenCookie = ResponseCookie.from("jwt", token)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(3600)
			// .domain("example.com") // Set to backend domain
			.build();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).build();
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch(DisabledException e) {
            System.out.println("Bad email and password");
        } catch(BadCredentialsException e) {
            System.out.println("Bad email and password");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
