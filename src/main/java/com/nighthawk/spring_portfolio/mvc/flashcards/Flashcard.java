package com.nighthawk.spring_portfolio.mvc.flashcards;

import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Flashcard {
  // id
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String front;
  private String back;

  @ManyToOne
  private FlashcardSet flashcardSet;
  
  // main class defining front and back for a flashcard with the given term and definition from the flashcard set
  public static void main(String[] args) {
        Flashcard f = new Flashcard();
        f.setFront("term");
        f.setBack("definition");

        System.out.println(f.toString());
  }
}
