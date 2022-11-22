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
@RequestMapping("/api/person")
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
    public ResponseEntity<Object> getPerson(@RequestBody final Map<String,Object> stat_map) throws NoSuchAlgorithmException {
        Optional<Person> optional = repository.findByEmail((String) stat_map.get("email"));
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
            String password = (String) stat_map.get("password");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);

            if (computedPasswordHash.equals(person.getPasswordHash())) {
                System.out.println(person.toString());
                return new ResponseEntity<>(person, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
            }
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
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
    @PostMapping( "/post")
    public ResponseEntity<Object> postPerson(@RequestBody final Map<String,Object> stat_map) throws NoSuchAlgorithmException {
        // A person object WITHOUT ID will create a new record with default roles as student
        Person person = new Person();
        person.setAge((int) stat_map.get("age"));
        person.setName((String) stat_map.get("name"));
        person.setEmail((String) stat_map.get("email"));
        person.setGender((String) stat_map.get("gender"));
        person.setHeightIn((int) stat_map.get("heightIn"));
        person.setWeightLbs((int) stat_map.get("weightLbs"));
        // password hash
        String password = (String) stat_map.get("password");
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
    @PostMapping(value = "/setStats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personStats(@RequestBody final Map<String,Object> stat_map) throws NoSuchAlgorithmException {
        Optional<Person> optional = repository.findByEmail((String) stat_map.get("email"));
        if (optional.isPresent()) {  // Good Email
            Person person = optional.get();  // value from findByEmail
            String password = (String) stat_map.get("password");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);
            if (!computedPasswordHash.equals(person.getPasswordHash())) {
                return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
            }

            int dayInt = (int) stat_map.get("day");
            int month = (int) stat_map.get("month");
            int year = (int) stat_map.get("year");
            int steps = (int) stat_map.get("steps");
            int calories = (int) stat_map.get("calories");
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
            // return Person with update Stats
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        
    }

}