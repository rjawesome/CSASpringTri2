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
public class PersonApiController {
    /*
     * #### RESTful API ####
     * Resource: https://spring.io/guides/gs/rest-service/
     */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private PersonJpaRepository repository;

    /*
     * GET individual Person using ID
     */
    @PostMapping("/getPerson")
    public ResponseEntity<Object> getPerson(@RequestBody final Map<String, Object> map)
            throws NoSuchAlgorithmException {

        /*
         * Fix findByEmail somehow because it needs to return User for JWT
         * Not my problem though
         */
        Optional<Person> optional = repository.findByEmail((String) map.get("email"));
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID
            String password = (String) map.get("password");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);

            if (computedPasswordHash.equals(person.getPasswordHash())) {
                // redact password
                person.passwordHash = "REDACTED";
                return new ResponseEntity<>(person, HttpStatus.OK);
            } else {
                Map<String, Object> resp = new HashMap<>();
                resp.put("err", "Incorrect Password");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }
        }
        // Bad ID
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "No account with this email");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    /*
     * DELETE individual Person using ID
     */
    @DeleteMapping("/deletePerson")
    public ResponseEntity<Object> deletePerson(@RequestBody final Map<String, Object> map)
            throws NoSuchAlgorithmException {
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        Optional<Person> optional = repository.findByEmail(email);
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);

            if (!computedPasswordHash.equals(person.passwordHash)) {
                Map<String, Object> resp = new HashMap<>();
                resp.put("err", "Incorrect Password");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            repository.deleteById(person.getId());

            Map<String, Object> resp = new HashMap<>();
            resp.put("err", false);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        // Bad ID
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "No account with this email");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    /*
     * POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/createPerson")
    public ResponseEntity<Object> postPerson(@RequestBody final Map<String, Object> map)
            throws NoSuchAlgorithmException {
        // check for existing person
        if (repository.findByEmail((String) map.get("email")).isPresent()) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Email already in use");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        // A person object WITHOUT ID will create a new record with default roles as
        // student
        Person person = new Person();
        person.setEmail((String) map.get("email"));

        // password hash
        String password = (String) map.get("password");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        String computedPasswordHash = new String(encodedHash);
        person.setPasswordHash(computedPasswordHash);

        repository.save(person);
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", false);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    // handles exceptions
    @ExceptionHandler({ ClassCastException.class, NullPointerException.class })
    public ResponseEntity<Object> handleBadUserInput() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Bad User Input");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}