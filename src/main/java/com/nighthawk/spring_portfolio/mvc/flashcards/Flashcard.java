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
}