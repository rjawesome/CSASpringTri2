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
    // sets variables for amount of mc questions or flashcard attempts incorrect and correct
    private int correct;
    private int incorrect;

    @ManyToOne
    private FlashcardSet flashcardSet;

    @ManyToOne
    private Flashcard flashcard;

    @ManyToOne
    private Person user;
    // main method for testing stats
    public static void main (String[] args) {
      Stats s = new Stats();
      s.setCorrect(10);
      s.setIncorrect(11);
      System.out.println(s.toString());
    }
  }
