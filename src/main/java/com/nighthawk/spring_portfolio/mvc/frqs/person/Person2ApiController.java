package com.nighthawk.spring_portfolio.mvc.frqs.person;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/person2")
public class Person2ApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private Person2JpaRepository repository;

    /*
    GET List of People
     */
    @GetMapping("/")
    public ResponseEntity<List<Person2>> getPeople() {
        return new ResponseEntity<>( repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    /*
    GET individual Person using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person2> getPerson(@PathVariable long id) {
        Optional<Person2> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person2 person = optional.get();  // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    @GetMapping("/bmi/{id}")
    public ResponseEntity<JsonNode> getBMI(@PathVariable long id) throws JsonMappingException, JsonProcessingException {
        Optional<Person2> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person2 person = optional.get();  // value from findByID
            ObjectMapper mapper = new ObjectMapper(); 
            JsonNode json = mapper.readTree("{ \"id\": "  +id+  ", " +  "\"BMI\": "  + person.getBMI() + "}");  // OK HTTP response: status code, headers, and body
            return ResponseEntity.ok(json);
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    @GetMapping("/age/{id}")
    public ResponseEntity<JsonNode> getAge(@PathVariable long id) throws JsonMappingException, JsonProcessingException {
        Optional<Person2> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person2 person = optional.get();  // value from findByID
            ObjectMapper mapper = new ObjectMapper(); 
            JsonNode json = mapper.readTree("{ \"id\": "  +id+  ", " +  "\"BMI\": "  + person.getAge() + "}");  // OK HTTP response: status code, headers, and body
            return ResponseEntity.ok(json);
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    @PostMapping("/goalAchieved")
    public ResponseEntity<Boolean> goalCheck(@RequestParam("id") long id, @RequestParam("date") String date) {
        Optional<Person2> optional = repository.findById(id);
        if (optional.isPresent()) {
            Person2 person = optional.get();
            long stepGoal = person.getStepGoal();
            System.out.println(stepGoal);
            JSONObject dayStat = new JSONObject(person.getDayStat(date));
            long stepsTaken = Long.valueOf((int) dayStat.get("steps"));

            return new ResponseEntity<>(stepsTaken >= stepGoal, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Person2> deletePerson(@PathVariable long id) {
        Optional<Person2> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person2 person = optional.get();  // value from findByID
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
    public ResponseEntity<Object> postPerson(@RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("name") String name,
                                             @RequestParam("dob") String dobString,
                                             @RequestParam("weight") double weight,
                                             @RequestParam("height") double height) {
        Date dob;
        try {
            dob = new SimpleDateFormat("MM-dd-yyyy").parse(dobString);
        } catch (Exception e) {
            return new ResponseEntity<>(dobString +" error; try MM-dd-yyyy", HttpStatus.BAD_REQUEST);
        }
        // A person object WITHOUT ID will create a new record with default roles as student
        Person2 person = new Person2(email, password, name, dob, weight, height);
        repository.save(person);
        return new ResponseEntity<>(email +" is created successfully", HttpStatus.CREATED);
    }

    /*
    The personSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personSearch(@RequestBody final Map<String,String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Person2> list = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
    The personStats API adds stats by Date to Person table 
    */
    @PostMapping(value = "/setStats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person2> personStats(@RequestBody final Map<String,Object> stat_map) {
        // find ID
        long id=Long.parseLong((String) stat_map.get("id"));  
        Optional<Person2> optional = repository.findById((id));
        if (optional.isPresent()) {  // Good ID
            Person2 person = optional.get();  // value from findByID

            // Extract Attributes from JSON
            Map<String, Object> attributeMap = new HashMap<>();
            for (Map.Entry<String,Object> entry : stat_map.entrySet())  {
                // Add all attribute other thaN "date" to the "attribute_map"
                if (!entry.getKey().equals("date") && !entry.getKey().equals("id"))
                    attributeMap.put(entry.getKey(), entry.getValue());
            }

            // Set Date and Attributes to SQL HashMap
            Map<String, Map<String, Object>> date_map = new HashMap<>();
            // date_map.put( (String) stat_map.get("date"), attributeMap );
            String date = (String) stat_map.get("date");
            person.addStats(date, attributeMap);  // BUG, needs to be customized to replace if existing or append if new
            repository.save(person);  // conclude by writing the stats updates

            // return Person with update Stats
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        
    }

}
