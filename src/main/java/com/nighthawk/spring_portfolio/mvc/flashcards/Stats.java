package com.nighthawk.spring_portfolio.mvc.flashcards;

import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stats {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private int correct;
    private int incorrect;

    @ManyToOne
    private FlashcardSet flashcardSet;

    @ManyToOne
    private Flashcard flashcard;

    @ManyToOne
    private Person user;
    
    public static void main (String[] args) {
      Stats s = new Stats();
      s.setCorrect(10);
      s.setIncorrect(11);
      System.out.println(s.toString());
    }
  }
