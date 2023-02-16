package com.nighthawk.spring_portfolio.mvc.flashcards;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginApiController {
  @Autowired
  LoginHandler handler;

  @Autowired
  PersonJpaRepository personJpaRepository;

  @PostMapping("/authenticate")
  public ResponseEntity<Object> authenticate(@RequestBody final Map<String, Object> map) throws NoSuchAlgorithmException {
    Person p = personJpaRepository.findByEmail((String) map.get("email")).get();
    if (p == null) {
      // error handling
    }

    String password = (String) map.get("password");

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedHash = digest.digest(
        password.getBytes(StandardCharsets.UTF_8));
    String computedPasswordHash = new String(encodedHash);

    System.out.println(password);
    System.out.println(computedPasswordHash + " -------- " + p.getPasswordHash());

    if (!computedPasswordHash.equals(p.getPasswordHash())) {
      // redact password
      p.passwordHash = "REDACTED";
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Incorrect Password");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    String jws = handler.createJwt(p);

    return new ResponseEntity<>(jws, HttpStatus.OK);
  }

  @PostMapping("/getYourUser")
  public ResponseEntity<Object> getYourUser(@RequestBody final Map<String, Object> map) {
    return new ResponseEntity<>(handler.decodeJwt((String) map.get("jwt")), HttpStatus.OK);
  }
}
