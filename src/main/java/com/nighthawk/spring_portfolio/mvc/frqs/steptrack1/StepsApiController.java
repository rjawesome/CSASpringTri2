package com.nighthawk.spring_portfolio.mvc.frqs.steptrack1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/steptrack1")
public class StepsApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private Person1JpaRepository repository;

    @Autowired
    private DayJpaRepository dayRepository;

    /*
    GET individual Person using ID
     */
    @PostMapping("/getPerson")
    public ResponseEntity<Object> getPerson(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
        Optional<Person1> optional = repository.findByEmail((String) map.get("email"));
        if (optional.isPresent()) {  // Good ID
            Person1 person = optional.get();  // value from findByID
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
    GET individual stats for a person using ID
     */
    @PostMapping("/getStats")
    public ResponseEntity<Object> getStats(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
        Optional<Person1> optional = repository.findByEmail((String) map.get("email"));
        if (optional.isPresent()) {  // Good ID
            Person1 person = optional.get();  // value from findByID
            String password = (String) map.get("password");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);

            if (!computedPasswordHash.equals(person.getPasswordHash())) {
              return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);
            }

            for (Day possibleDay : person.getDays()) {
              if (possibleDay.isDate((int) map.get("day"), (int) map.get("month"), (int) map.get("year"))) {
                return new ResponseEntity<>(possibleDay, HttpStatus.OK);
              }
            }
            return new ResponseEntity<>("No stats for that day", HttpStatus.BAD_REQUEST);       
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
        Optional<Person1> optional = repository.findByEmail(email);
        if (optional.isPresent()) {  // Good ID
            Person1 person = optional.get();  // value from findByID
            
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
        Person1 person = new Person1();
        person.setAge((int) map.get("age"));
        person.setName((String) map.get("name"));
        person.setEmail((String) map.get("email"));
        person.setGender((String) map.get("gender"));
        person.setHeightIn((int) map.get("heightIn"));
        person.setWeightLbs((int) map.get("weightLbs"));

        int activeSteps = (int) ((((double) person.getWeightLbs() / (person.getHeightIn() * person.getHeightIn())) * 703) * 500);
        person.setActiveSteps(activeSteps);

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

    /*
    The personStats API adds stats by Date to Person table 
    */
    @PostMapping(value = "/setStats")
    public ResponseEntity<Object> personStats(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
        Optional<Person1> optional = repository.findByEmail((String) map.get("email"));
        if (optional.isPresent()) {  // Good Email
            Person1 person = optional.get();  // value from findByEmail
            String password = (String) map.get("password");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);
            if (!computedPasswordHash.equals(person.getPasswordHash())) {
                return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
            }

            int dayInt = (int) map.get("day");
            int month = (int) map.get("month");
            int year = (int) map.get("year");
            int steps = (int) map.get("steps");
            int calories = (int) map.get("calories");
            double miles = (double) steps / 2250;

            //check if the day already exists
            boolean found = false;
            for (Day existingDay : person.getDays()) {
              if (existingDay.isDate(dayInt, month, year)) {
                existingDay.appendCalories(calories);
                existingDay.appendDistance(miles);
                existingDay.appendSteps(steps);
                dayRepository.save(existingDay);
                found = true;
                break;
              }
            }

            if (!found) {
              Day day = new Day();  
              day.setCalories(calories);
              day.setSteps(steps);
              day.setDay(dayInt);
              day.setMonth(month);
              day.setYear(year);
              day.setDistanceMiles(miles);
              dayRepository.save(day);
              person.addDay(day);
              repository.save(person);
            }

            //redact password
            person.passwordHash = "REDACTED";

            // return Person with update Stats
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>("Account doesn't exist", HttpStatus.BAD_REQUEST); 
    }

    // handles exceptions
    @ExceptionHandler({ClassCastException.class, NullPointerException.class})
    public ResponseEntity<Object> handleBadUserInput () {
      return new ResponseEntity<>("Bad input JSON", HttpStatus.BAD_REQUEST); 
    }
}