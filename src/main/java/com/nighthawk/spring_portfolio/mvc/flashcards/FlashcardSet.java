package com.nighthawk.spring_portfolio.mvc.flashcards;

import java.util.List;

import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FlashcardSet {
  // id
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  private boolean isPublic;

  @ManyToOne
  private Person owner;
  
  public static void main (String[] args) {
    FlashcardSet fs = new FlashcardSet();
    fs.setName("Cool set");
    fs.setPublic(true);
    System.out.println(fs.toString());
  }
}
