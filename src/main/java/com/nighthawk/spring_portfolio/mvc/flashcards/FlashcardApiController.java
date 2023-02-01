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
@RequestMapping("/api/flashcard")
public class FlashcardApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private UserJpaRepository repository;

    @Autowired
    private FlashcardSetJpaRepository flashcardSetRepository;
    /*
    GET individual Person using ID
     */
    @PostMapping("/createFlashcardSet")
    public ResponseEntity<Object> createFlashcardSet(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {

        /*
         * Fix findByEmail somehow because it needs to return User for JWT
         * Not my problem though
         */
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
            }
            else {
                return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
            }

            List<Map<String, Object>> flashcardData = (List<Map<String, Object>>) map.get("flashcards");

            FlashcardSet flashcardSet = new FlashcardSet();
            flashcardSet.setName((String) map.get("name"));
            flashcardSet.setOwner(person);
            flashcardSet.setPublic((boolean) map.get("isPublic"));

            for (Map<String, Object> flashcard : flashcardData) {
                Flashcard flashcardObject = new Flashcard();
                flashcardObject.setFront((String) flashcard.get("front"));
                flashcardObject.setBack((String) flashcard.get("back"));
                flashcardObject.setFlashcardSet(flashcardSet);
                // flashcardSet.getFlashcards().add(flashcardObject);
            }
        }
        // Bad ID
        return new ResponseEntity<>("Person with email doesn't exist", HttpStatus.BAD_REQUEST);       
    }

   
    @PostMapping("/getFlashcardSet")
    public ResponseEntity<Object> getFlashcardSet(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {

        /*
         * Fix findByEmail somehow because it needs to return User for JWT
         * Not my problem though
         */
        Optional <FlashcardSet> optionalFlashcardSet = flashcardSetRepository.findById((long) map.get("id"));
        if (!optionalFlashcardSet.isPresent())  {
            return new ResponseEntity<>("Flashcard set doesn't exist", HttpStatus.BAD_REQUEST);        
        }
        if (!optionalFlashcardSet.get().isPublic()) {
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
                }
                else {
                    return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
                }
        }


            
        }

        return new ResponseEntity<>(optionalFlashcardSet.get(), HttpStatus.OK);       
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