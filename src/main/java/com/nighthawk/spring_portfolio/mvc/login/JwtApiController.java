package com.nighthawk.spring_portfolio.mvc.login;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nighthawk.spring_portfolio.mvc.flashcards.Person;
import com.nighthawk.spring_portfolio.mvc.flashcards.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.flashcards.PersonJpaRepository;
import com.nighthawk.spring_portfolio.security.SecurityConfig;

@RestController
@RequestMapping("/api/jwt")
public class JwtApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private PersonDetailsService personDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> map) throws Exception {

        System.out.println("LIST BEGIN");
        System.out.println(personJpaRepository.findAll());
        System.out.println("LIST END");
        // Creating password hash from password
        // MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // byte[] encodedHash = digest.digest(
        // map.get("password").getBytes(StandardCharsets.UTF_8));
        // String computedPasswordHash = new String(encodedHash);
        String computedPasswordHash = SecurityConfig.bcryptencode(map.get("password"));
        Person authenticationRequest = personJpaRepository.findByEmailAndPasswordHash((String) map.get("email"), computedPasswordHash);

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
        System.out.println(tokenCookie);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).build();
    }

    // Worry about it later
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody String email, @RequestBody String password) throws Exception {

        return null;
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
