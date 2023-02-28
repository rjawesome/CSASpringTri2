package com.nighthawk.spring_portfolio.mvc.flashcards;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler {
  Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  @Autowired
  PersonJpaRepository personJpaRepository;

  public String createJwt(Person user) {
    return Jwts.builder().setSubject(user.getEmail()).signWith(key).compact();
  }

  public Person decodeJwt(String jws) {
    try {
      String email = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject();
      return personJpaRepository.findByEmail(email).get();
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public static void main(String[] args) {
    LoginHandler handler = new LoginHandler();
    String email = "rohanj2006@gmail.com";
    Person p = new Person();
    p.setEmail(email);
    String jws = handler.createJwt(p);
    System.out.println(jws);
    System.out.println(handler.decodeJwt(jws));
  }
}
