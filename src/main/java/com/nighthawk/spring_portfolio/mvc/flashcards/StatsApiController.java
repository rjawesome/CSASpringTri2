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
@RequestMapping("/api/stats")
public class StatsApiController {
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

    @Autowired
    private StatsJpaRepository statsRepository;

    @PostMapping("/getStatsByFlashcardSet")
    public ResponseEntity<Object> getStatsByFlashcardSet(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
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
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Incorrect Password");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);        
          }
      } else {
          Map<String, Object> resp = new HashMap<>();
          resp.put("err", "Incorrect Password");
          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);           
       } 

       List<Stats> stats = statsRepository.findByUserAndFlashcardSet(optional.get(), flashcardSetRepository.findById((int) map.get("id")).get());
       return new ResponseEntity<>(stats, HttpStatus.OK);
    } 

    @PostMapping("/getStatsByFlashcard")
    public ResponseEntity<Object> getStatsByFlashcard(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
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
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Incorrect Password");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);        
          }
      } else {
          Map<String, Object> resp = new HashMap<>();
          resp.put("err", "Incorrect Password");
          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);           
       } 

       List<Stats> stats = statsRepository.findByUserAndFlashcard(optional.get(), flashcardRepository.findById((int) map.get("id")).get());
       return new ResponseEntity<>(stats, HttpStatus.OK);
    } 
  
    @PostMapping("/createStats")
    public ResponseEntity<Object> createStats(@RequestBody final Map<String,Object> map) throws NoSuchAlgorithmException {
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
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Incorrect Password");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);        
          }
      } else {
          Map<String, Object> resp = new HashMap<>();
          resp.put("err", "Incorrect Password");
          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);           
       } 

       Optional<Flashcard> flash = flashcardRepository.findById((int) map.get("id"));
       if (!flash.isPresent()) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Flashcard doesn't exist");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
       }

       List<Stats> stats = statsRepository.findByUserAndFlashcard(optional.get(), flash.get());
       if (stats.isEmpty()) {
          Stats newStats = new Stats();
          newStats.setUser(optional.get());
          newStats.setFlashcard(flash.get());
          newStats.setFlashcardSet(flash.get().getFlashcardSet());
          if ((boolean) map.get("correct")) stats.get(0).setCorrect(1);
          else stats.get(0).setIncorrect(1);
          statsRepository.save(newStats);
          return new ResponseEntity<>(newStats, HttpStatus.OK);
       } else {
          if ((boolean) map.get("correct")) stats.get(0).setCorrect(stats.get(0).getCorrect() + 1);
          else stats.get(0).setIncorrect(stats.get(0).getIncorrect() + 1);
          statsRepository.save(stats.get(0));
          return new ResponseEntity<>(stats.get(0), HttpStatus.OK);
       }

    } 

    // handles exceptions
    @ExceptionHandler({ClassCastException.class, NullPointerException.class})
    public ResponseEntity<Object> handleBadUserInput () {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Bad user input");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}