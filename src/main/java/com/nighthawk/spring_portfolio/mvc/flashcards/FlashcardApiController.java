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

  /*
   * GET individual Person using ID
   */
  @PostMapping("/createFlashcardSet")
  public ResponseEntity<Object> createFlashcardSet(@RequestBody final Map<String, Object> map)
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
      } else {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Incorrect Password");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
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
      Map<String, Object> resp = new HashMap<>();
      resp.put("id", flashcardSet.getId());
      return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    // Bad ID
    Map<String, Object> resp = new HashMap<>();
    resp.put("err", "Account doesn't exist");
    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/getYourFlashcardSets")
  public ResponseEntity<Object> getYourFlashcardSets(@RequestBody final Map<String, Object> map)
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
        // auth passed
      } else {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Incorrect Password");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Account doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    List<FlashcardSet> flashcardSets = flashcardSetRepository.findAllByOwnerEmail(optional.get().getEmail());

    for (FlashcardSet i : flashcardSets) {
      i.getOwner().setPasswordHash("REDACTED");
      ;
    }

    return new ResponseEntity<>(flashcardSets, HttpStatus.OK);
  }

  @PostMapping("/getFlashcardSet")
  public ResponseEntity<Object> getFlashcardSet(@RequestBody final Map<String, Object> map)
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
    if (!flashcardSet.isPublic()) {
      Optional<Person> optional = repository.findByEmail((String) map.get("email"));

      if (optional.isPresent()) { // Good ID
        Person person = optional.get(); // value from findByID
        String password = (String) map.get("password");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
            password.getBytes(StandardCharsets.UTF_8));
        String computedPasswordHash = new String(encodedHash);

        if (computedPasswordHash.equals(person.getPasswordHash())) {
          // auth passed
        } else {
          Map<String, Object> resp = new HashMap<>();
          resp.put("err", "Incorrect Password");
          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
      } else {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Incorrect Password");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }
    }

    flashcardSet.getOwner().setPasswordHash("REDACTED");
    ;

    List<Flashcard> flashcards = flashcardRepository.findByFlashcardSet(flashcardSet);
    for (Flashcard i : flashcards) {
      i.setFlashcardSet(null);
    }
    HashMap<String, Object> resp = new HashMap<String, Object>();
    resp.put("meta", flashcardSet);
    resp.put("flashcards", flashcards);

    return new ResponseEntity<>(resp, HttpStatus.OK);
  }

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

  @PostMapping("/getFlashcardSetMC")
  public ResponseEntity<Object> getFlashcardSetMC(@RequestBody final Map<String, Object> map)
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
        } else {
          Map<String, Object> resp = new HashMap<>();
          resp.put("err", "Incorrect Password");
          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
      } else {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Incorrect Password");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }
    }

    Map<String, Map<String, Object>> mcq = new HashMap<>();
    List<Flashcard> flashcards = flashcardRepository.findByFlashcardSet(optionalFlashcardSet.get());

    if (flashcards.size() < 4) {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Flashcard set too small");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

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
}
