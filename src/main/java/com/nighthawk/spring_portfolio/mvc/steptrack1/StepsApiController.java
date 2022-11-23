package com.nighthawk.spring_portfolio.mvc.steptrack1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/steptrack1")
public class StepsApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private PersonJpaRepository repository;

    @Autowired
    private DayJpaRepository dayRepository;

    /*
    GET individual Person using ID
     */
    @GetMapping("/getPerson")
    public ResponseEntity<Object> getPerson(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
        Optional<Person> optional = repository.findByEmail((String) map.get("email"));
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
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
    @DeleteMapping("/deletePerson/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
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
        Person person = new Person();
        person.setAge((int) map.get("age"));
        person.setName((String) map.get("name"));
        person.setEmail((String) map.get("email"));
        person.setGender((String) map.get("gender"));
        person.setHeightIn((int) map.get("heightIn"));
        person.setWeightLbs((int) map.get("weightLbs"));


        /* CHANGE TO A CALCULATION */
        person.setActiveSteps(10000);


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
        Optional<Person> optional = repository.findByEmail((String) map.get("email"));
        if (optional.isPresent()) {  // Good Email
            Person person = optional.get();  // value from findByEmail
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
              if (existingDay.getDay() == dayInt && existingDay.getMonth() == month && existingDay.getYear() == year) {
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

}