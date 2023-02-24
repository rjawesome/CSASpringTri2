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
   * #### RESTful API ####
   * Resource: https://spring.io/guides/gs/rest-service/
   */

  // Autowired enables Control to connect POJO Object through JPA
  @Autowired
  private LoginHandler handler;

  @Autowired
  private PersonJpaRepository repository;

  @Autowired
  private FlashcardSetJpaRepository flashcardSetRepository;

  @Autowired
  private FlashcardJpaRepository flashcardRepository;

  @Autowired
  private StatsJpaRepository statsRepository;

  @PostMapping("/getStatsByFlashcardSet")
  public ResponseEntity<Object> getStatsByFlashcardSet(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt)
      throws NoSuchAlgorithmException {
    Person p = handler.decodeJwt(jwt);

    if (p == null) {
      // return err ting
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Account doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    List<Stats> stats = statsRepository.findByUserAndFlashcardSet(p,
        flashcardSetRepository.findById((int) map.get("id")).get());
    
    for (var stat : stats) {
      stat.setFlashcardSet(null);
      stat.getFlashcard().setFlashcardSet(null);
      stat.setUser(null);
    }
    
    return new ResponseEntity<>(stats, HttpStatus.OK);
  }
  // gets stats for each flashcard based on the id
  @PostMapping("/getStatsByFlashcard")
  public ResponseEntity<Object> getStatsByFlashcard(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt)
      throws NoSuchAlgorithmException {
        Person p = handler.decodeJwt(jwt);

    if (p == null) {
      // return err ting
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Account doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    List<Stats> stats = statsRepository.findByUserAndFlashcard(p,
        flashcardRepository.findById((int) map.get("id")).get());
    return new ResponseEntity<>(stats, HttpStatus.OK);
  }
  // stats calculations
  @PostMapping("/createStats")
  public ResponseEntity<Object> createStats(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt)
      throws NoSuchAlgorithmException {
    
        Person p = handler.decodeJwt(jwt);

        if (p == null) {
          // return err ting
          Map<String, Object> resp = new HashMap<>();
          resp.put("err", "Account doesn't exist");
          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

    Optional<Flashcard> flash = flashcardRepository.findById((int) map.get("id"));
    if (!flash.isPresent()) {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Flashcard doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    // checks if states exist, uses boolean if someone gets an answer right or wrong
    List<Stats> stats = statsRepository.findByUserAndFlashcard(p, flash.get());
    if (stats.isEmpty()) {
      Stats newStats = new Stats();
      newStats.setUser(p);
      newStats.setFlashcard(flash.get());
      newStats.setFlashcardSet(flash.get().getFlashcardSet());
      if ((boolean) map.get("correct"))
        newStats.setCorrect(1);
      else
        newStats.setIncorrect(1);
      statsRepository.save(newStats);
    } else {
      if ((boolean) map.get("correct"))
        stats.get(0).setCorrect(stats.get(0).getCorrect() + 1);
      else
        stats.get(0).setIncorrect(stats.get(0).getIncorrect() + 1);
      statsRepository.save(stats.get(0));
    }
    Map<String, Object> resp = new HashMap<>();
    resp.put("err", false);
    return new ResponseEntity<>(resp, HttpStatus.OK);
  }
  // checks if a person's account exists with jwt
  @PostMapping("/createStatsBatch")
  public ResponseEntity<Object> createStatsBatch(@RequestBody final Map<String, Object> map,  @CookieValue("flashjwt") String jwt)
      throws NoSuchAlgorithmException {
    Person p = handler.decodeJwt(jwt);

        if (p == null) {
          // return err ting
          Map<String, Object> resp = new HashMap<>();
          resp.put("err", "Account doesn't exist");
          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

    List<Map<String, Object>> statsList = (List<Map<String, Object>>) map.get("statsList");

    for (var stat : statsList) {
      Optional<Flashcard> flash = flashcardRepository.findById((int) stat.get("id"));
      if (!flash.isPresent()) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Flashcard doesn't exist");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }

      List<Stats> stats = statsRepository.findByUserAndFlashcard(p, flash.get());
      if (stats.isEmpty()) {
        Stats newStats = new Stats();
        newStats.setUser(p);
        newStats.setFlashcard(flash.get());
        newStats.setFlashcardSet(flash.get().getFlashcardSet());
        if ((boolean) stat.get("correct"))
          newStats.setCorrect(1);
        else
          newStats.setIncorrect(1);
        statsRepository.save(newStats);
      } else {
        if ((boolean) stat.get("correct"))
          stats.get(0).setCorrect(stats.get(0).getCorrect() + 1);
        else
          stats.get(0).setIncorrect(stats.get(0).getIncorrect() + 1);
        statsRepository.save(stats.get(0));
      }
    }
    Map<String, Object> resp = new HashMap<>();
    resp.put("err", false);
    return new ResponseEntity<>(resp, HttpStatus.OK);
  }

  // handles exceptions
  @ExceptionHandler({ ClassCastException.class, NullPointerException.class })
  public ResponseEntity<Object> handleBadUserInput() {
    Map<String, Object> resp = new HashMap<>();
    resp.put("err", "Bad user input");
    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }
}
