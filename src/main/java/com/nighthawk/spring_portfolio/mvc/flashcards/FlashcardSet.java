package com.nighthawk.spring_portfolio.mvc.flashcards;

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
    private User owner;
  }