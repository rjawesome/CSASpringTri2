package com.nighthawk.spring_portfolio.mvc.flashcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/flashcard")
public class FlashcardApiController {
  /*
   * #### RESTful API ####
   * Resource: https://spring.io/guides/gs/rest-service/
   */

  // Autowired enables Control to connect POJO Object through JPA
  @Autowired
  private PersonJpaRepository repository;

  @Autowired
  private FlashcardSetJpaRepository flashcardSetRepository;

  @Autowired
  private FlashcardJpaRepository flashcardRepository;

  @Autowired
  private LoginHandler handler;

  /*
   * GET individual Person using ID
   */
  @PostMapping("/createFlashcardSet")
  public ResponseEntity<Object> createFlashcardSet(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt)
      throws NoSuchAlgorithmException {

    /*
     * Fix findByEmail somehow because it needs to return User for JWT
     * Not my problem though
     */
    Person person = handler.decodeJwt(jwt);
    // if person doesn't exist, return an error that they don't have an account
    if (person == null) {
      // return err ting
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Account doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

      List<Map<String, Object>> flashcardData = (List<Map<String, Object>>) map.get("flashcards");
      // flashcard set is created with attributes of name, owner, whether or not it is is public, set is saved
      FlashcardSet flashcardSet = new FlashcardSet();
      flashcardSet.setName((String) map.get("name"));
      flashcardSet.setOwner(person);
      flashcardSet.setPublic((boolean) map.get("isPublic"));
      flashcardSetRepository.save(flashcardSet);
      // for each term/definition in the flashcard set, add it to the front and back of a flashcard
      for (Map<String, Object> flashcard : flashcardData) {
        Flashcard flashcardObject = new Flashcard();
        flashcardObject.setFront((String) flashcard.get("front"));
        flashcardObject.setBack((String) flashcard.get("back"));
        flashcardObject.setFlashcardSet(flashcardSet);
        flashcardRepository.save(flashcardObject);
        // flashcardSet.getFlashcards().add(flashcardObject);
      }
      Map<String, Object> resp = new HashMap<>();
      resp.put("id", flashcardSet.getId());
      return new ResponseEntity<>(resp, HttpStatus.OK);
    
    }
  
  /*
   * @param {Map<String, Object>} map - should reference "name" and "isPublic" of flashcard set; probably should use id but who cares
   */
  @DeleteMapping("/deleteFlashcardSet")
  public ResponseEntity<Object> deleteFlashcardSet(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt) {
    Person p = handler.decodeJwt(jwt);
    if (p.isAdmin()) {

      long id = (long) map.get("id");
      Optional<FlashcardSet> optional = flashcardSetRepository.findById(id);

      if (optional.isPresent()) {
        FlashcardSet flashcardSet = optional.get();
        flashcardSetRepository.deleteById(flashcardSet.getId());
        // Success
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", false);
        return new ResponseEntity<>(resp, HttpStatus.OK);
      }

      // Bad ID
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "No flashcard set found");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);

    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Unauthorized");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

  }
  // get flashcard set
  @PostMapping("/getYourFlashcardSets")
  public ResponseEntity<Object> getYourFlashcardSets(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt)
      throws NoSuchAlgorithmException {

    /*
     * Fix findByEmail somehow because it needs to return User for JWT
     * Not my problem though
     */
    Person person = handler.decodeJwt(jwt);

    if (person == null) {
      // return err ting
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Account doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    List<FlashcardSet> flashcardSets = flashcardSetRepository.findAllByOwnerEmail(person.getEmail());
    // check password hash to see if the person can access the flashcard set
    for (FlashcardSet i : flashcardSets) {
      i.getOwner().setPasswordHash("REDACTED");
    }

    return new ResponseEntity<>(flashcardSets, HttpStatus.OK);
  }
  // gets flashcard set using jwt
  @PostMapping("/getFlashcardSet")
  public ResponseEntity<Object> getFlashcardSet(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt)
      throws NoSuchAlgorithmException {

    /*
     * Fix findByEmail somehow because it needs to return User for JWT
     * Not my problem though
     */
    Optional<FlashcardSet> optionalFlashcardSet = flashcardSetRepository.findById((int) map.get("id"));
    if (!optionalFlashcardSet.isPresent()) {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Flashcard set doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    FlashcardSet flashcardSet = optionalFlashcardSet.get();
    // checks if the flashcard set is public
    if (!flashcardSet.isPublic()) {
      Person person = handler.decodeJwt(jwt);
      // if person doesn't exist, returns error
      if (person == null) {
        // return err ting
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Account doesn't exist");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }

      if (person.getEmail() != flashcardSet.getOwner().getEmail()) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Flashcard set is private");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }
    }

    flashcardSet.getOwner().setPasswordHash("REDACTED");

    List<Flashcard> flashcards = flashcardRepository.findByFlashcardSet(flashcardSet);
    for (Flashcard i : flashcards) {
      i.setFlashcardSet(null);
    }
    HashMap<String, Object> resp = new HashMap<String, Object>();
    resp.put("meta", flashcardSet);
    resp.put("flashcards", flashcards);

    return new ResponseEntity<>(resp, HttpStatus.OK);
  }
  // gets flashcard by name for the search bar
  @PostMapping("/getFlashcardSetsByName")
  public ResponseEntity<Object> getFlashcardSetsByName(@RequestBody final Map<String, Object> map)
      throws NoSuchAlgorithmException {

    /*
     * Fix findByEmail somehow because it needs to return User for JWT
     * Not my problem though
     */
    List<FlashcardSet> flashcardSets = flashcardSetRepository
        .findByNameContainingIgnoreCaseAndIsPublic((String) map.get("name"), true);

    for (FlashcardSet i : flashcardSets) {
      i.getOwner().setPasswordHash("REDACTED");
    }

    return new ResponseEntity<>(flashcardSets, HttpStatus.OK);
  }

  /*
   * GET MC quiz for flashcard set
   */
  // gets flashcard set mc to be used for search
  @PostMapping("/getFlashcardSetMC")
  public ResponseEntity<Object> getFlashcardSetMC(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt)
      throws NoSuchAlgorithmException {

    /*
     * Fix findByEmail somehow because it needs to return User for JWT
     * Not my problem though
     */
    Optional<FlashcardSet> optionalFlashcardSet = flashcardSetRepository.findById((int) map.get("id"));
    if (!optionalFlashcardSet.isPresent()) {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Flashcard set doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    if (!optionalFlashcardSet.get().isPublic()) {
      Person person = handler.decodeJwt(jwt);
      // if person doesn't exist return error
      if (person == null) {
        // return err ting
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Account doesn't exist");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }
      // if person exists but the public option is not checked, display error that the set is private
      if (person.getEmail() != optionalFlashcardSet.get().getOwner().getEmail()) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Flashcard set is private");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }
    }

    Map<String, Map<String, Object>> mcq = new HashMap<>();
    List<Flashcard> flashcards = flashcardRepository.findByFlashcardSet(optionalFlashcardSet.get());
    // sets minimum size of flashcard set
    if (flashcards.size() < 4) {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Flashcard set too small");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    // creates question and answers from the flashcard set for the mcq
    for (int i = 0; i < flashcards.size(); i++) {
      Flashcard flashcard = flashcards.get(i);
      String question = flashcard.getFront();
      String answer = flashcard.getBack();

      mcq.put(question, new HashMap<>());
      var answers = new ArrayList<String>();
      mcq.get(question).put("answers", answers);
      answers.add(answer);

      // wrong answers
      Set<Integer> prevAns = new HashSet<>();
      for (int j = 0; j < 3; j++) {
        int randAns = -1;
        while (randAns == -1 || randAns == i || prevAns.contains(randAns)) {
          randAns = (int) (Math.random() * flashcards.size());
        }
        answers.add(flashcards.get(randAns).getBack());
        prevAns.add(randAns);
      }
      mcq.get(question).put("id", flashcard.getId());
    }

    return new ResponseEntity<>(mcq, HttpStatus.OK);
  }
  // imports quizlet using fetch method from quizlet.java file
  @CrossOrigin("*")
  @PostMapping("/getQuizlet")
  public ResponseEntity<Object> getQuizlet(@RequestBody final Map<String, Object> map)
      throws NoSuchAlgorithmException, InterruptedException {
    return new ResponseEntity<>(Quizlet.fetch((String) map.get("id")), HttpStatus.OK);
  }

  // handles exceptions
  @ExceptionHandler({ ClassCastException.class, NullPointerException.class })
  public ResponseEntity<Object> handleBadUserInput() {
    Map<String, Object> resp = new HashMap<>();
    resp.put("err", "Bad user input");
    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler({ MissingRequestCookieException.class })
  public ResponseEntity<Object> handleNoCookie() {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Account doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }
}
