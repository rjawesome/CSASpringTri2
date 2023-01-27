package com.nighthawk.spring_portfolio.mvc.flashcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private UserJpaRepository repository;

    /*
    GET individual Person using ID
     */
    @PostMapping("/getPerson")
    public ResponseEntity<Object> getPerson(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
        Optional<User> optional = repository.findByEmail((String) map.get("email"));
        if (optional.isPresent()) {  // Good ID
            User person = optional.get();  // value from findByID
            String password = (String) map.get("password");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);

            if (computedPasswordHash.equals(person.getPasswordHash())) {
                // redact password
                person.passwordHash = "REDACTED";
                return new ResponseEntity<>(person, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
            }
        }
        // Bad ID
        return new ResponseEntity<>("Person with email doesn't exist", HttpStatus.BAD_REQUEST);       
    }

   

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/deletePerson")
    public ResponseEntity<Object> deletePerson(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        Optional<User> optional = repository.findByEmail(email);
        if (optional.isPresent()) {  // Good ID
            User person = optional.get();  // value from findByID
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);

            if (!computedPasswordHash.equals(person.passwordHash)) {
                return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST); 
            }

            repository.deleteById(person.getId());

            return new ResponseEntity<>("Person has been deleted", HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>("Person doesn't exist", HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/createPerson")
    public ResponseEntity<Object> postPerson(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
        //check for existing person
        if (repository.findByEmail((String) map.get("email")).isPresent()) {
            return new ResponseEntity<>("Account with email has been created", HttpStatus.CREATED);
        }


        // A person object WITHOUT ID will create a new record with default roles as student
        User person = new User();
        person.setEmail((String) map.get("email"));

        // password hash
        String password = (String) map.get("password");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
        password.getBytes(StandardCharsets.UTF_8));
        String computedPasswordHash = new String(encodedHash);
        person.setPasswordHash(computedPasswordHash);
        
        repository.save(person);
        return new ResponseEntity<>("Account is created successfully", HttpStatus.CREATED);
    }

  
    // handles exceptions
    @ExceptionHandler({ClassCastException.class, NullPointerException.class})
    public ResponseEntity<Object> handleBadUserInput () {
      return new ResponseEntity<>("Bad input JSON", HttpStatus.BAD_REQUEST); 
    }
}