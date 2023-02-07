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
    private PersonJpaRepository repository;

    @Autowired
    private FlashcardSetJpaRepository flashcardSetRepository;

    @Autowired
    private FlashcardJpaRepository flashcardRepository;

    /*
    GET individual Person using ID
     */
    @PostMapping("/createFlashcardSet")
    public ResponseEntity<Object> createFlashcardSet(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {

        /*
         * Fix findByEmail somehow because it needs to return User for JWT
         * Not my problem though
         */
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
            }
            else {
                return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
            }

            List<Map<String, Object>> flashcardData = (List<Map<String, Object>>) map.get("flashcards");

            FlashcardSet flashcardSet = new FlashcardSet();
            flashcardSet.setName((String) map.get("name"));
            flashcardSet.setOwner(person);
            flashcardSet.setPublic((boolean) map.get("isPublic"));
            flashcardSetRepository.save(flashcardSet);

            for (Map<String, Object> flashcard : flashcardData) {
                Flashcard flashcardObject = new Flashcard();
                flashcardObject.setFront((String) flashcard.get("front"));
                flashcardObject.setBack((String) flashcard.get("back"));
                flashcardObject.setFlashcardSet(flashcardSet);
                flashcardRepository.save(flashcardObject);
                // flashcardSet.getFlashcards().add(flashcardObject);
            }
            return new ResponseEntity<>("Flashcard Set created with ID " + flashcardSet.getId(), HttpStatus.OK);
        }
        // Bad ID
        return new ResponseEntity<>("Person with email doesn't exist", HttpStatus.BAD_REQUEST);       
    }

    @PostMapping("/getYourFlashcardSets")
    public ResponseEntity<Object> getYourFlashcardSets(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
      
        /*
         * Fix findByEmail somehow because it needs to return User for JWT
         * Not my problem though
         */
        Optional<Person> optional = repository.findByEmail((String) map.get("email"));

        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
            String password = (String) map.get("password");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);

            if (computedPasswordHash.equals(person.getPasswordHash())) {
              // auth passed
            }
            else {
                return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
            }
        } else {
          return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        }  

        List<FlashcardSet> flashcardSets = flashcardSetRepository.findAllByOwnerEmail(optional.get().getEmail());
        
        for (FlashcardSet i : flashcardSets) {
          i.getOwner().setPasswordHash("REDACTED");;
        }

        return new ResponseEntity<>(flashcardSets, HttpStatus.OK);
    }
   
    @PostMapping("/getFlashcardSet")
    public ResponseEntity<Object> getFlashcardSet(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {

        /*
         * Fix findByEmail somehow because it needs to return User for JWT
         * Not my problem though
         */
        Optional <FlashcardSet> optionalFlashcardSet = flashcardSetRepository.findById((int) map.get("id"));
        if (!optionalFlashcardSet.isPresent())  {
            return new ResponseEntity<>("Flashcard set doesn't exist", HttpStatus.BAD_REQUEST);        
        }
        FlashcardSet flashcardSet = optionalFlashcardSet.get();
        if (!flashcardSet.isPublic()) {
            Optional<Person> optional = repository.findByEmail((String) map.get("email"));

            if (optional.isPresent()) {  // Good ID
                Person person = optional.get();  // value from findByID
                String password = (String) map.get("password");
    
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedHash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
                String computedPasswordHash = new String(encodedHash);
    
                if (computedPasswordHash.equals(person.getPasswordHash())) {
                  // auth passed
                }
                else {
                    return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
                }
            } else {
              return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);          
            } 
        }

        flashcardSet.getOwner().setPasswordHash("REDACTED");;

        List<Flashcard> flashcards = flashcardRepository.findByFlashcardSet(flashcardSet);
        for (Flashcard i : flashcards) {
          i.setFlashcardSet(null);
        }
        HashMap<String, Object> resp = new HashMap<String, Object>();
        resp.put("meta", flashcardSet);
        resp.put("flashcards", flashcards);

        return new ResponseEntity<>(resp, HttpStatus.OK);       
    }

    /*
     * GET MC quiz for flashcard set
     */

    @PostMapping("/getFlashcardSetMC")
    public ResponseEntity<Object> getFlashcardSetMC(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {

        /*
         * Fix findByEmail somehow because it needs to return User for JWT
         * Not my problem though
         */
        Optional <FlashcardSet> optionalFlashcardSet = flashcardSetRepository.findById((int) map.get("id"));
        if (!optionalFlashcardSet.isPresent())  {
            return new ResponseEntity<>("Flashcard set doesn't exist", HttpStatus.BAD_REQUEST);        
        }
        if (!optionalFlashcardSet.get().isPublic()) {
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
                }
                else {
                    return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);        
                }
          }
        }

        Map<String, List<String>> mcq = new HashMap<>();
        List<Flashcard> flashcards = flashcardRepository.findByFlashcardSet(optionalFlashcardSet.get());

        if (flashcards.size() < 4) {
          return new ResponseEntity<>("Flashcard Set too small", HttpStatus.BAD_REQUEST); 
        }

        for (int i = 0; i<flashcards.size(); i++) {
          Flashcard flashcard = flashcards.get(i);
          String question = flashcard.getFront();
          String answer = flashcard.getBack();

          mcq.put(question, new ArrayList<>());
          mcq.get(question).add(answer);

          // wrong answers
          Set<Integer> prevAns = new HashSet<>();
          for (int j = 0; j<3; j++) {
            int randAns = -1;
            while (randAns == -1 || randAns == i || prevAns.contains(randAns)){
              randAns = (int)(Math.random()*flashcards.size());
            }
            mcq.get(question).add(flashcards.get(randAns).getBack());
            prevAns.add(randAns);
          }
        }

        return new ResponseEntity<>(mcq, HttpStatus.OK);       
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/deletePerson")
    public ResponseEntity<Object> deletePerson(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        Optional<Person> optional = repository.findByEmail(email);
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
            
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
        return new ResponseEntity<>("Account is created successfully", HttpStatus.CREATED);
    }

  
    // handles exceptions
    @ExceptionHandler({ClassCastException.class, NullPointerException.class})
    public ResponseEntity<Object> handleBadUserInput () {
      return new ResponseEntity<>("Bad input JSON", HttpStatus.BAD_REQUEST); 
    }
}