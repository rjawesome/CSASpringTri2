package com.nighthawk.spring_portfolio.mvc.flashcards;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface FlashcardJpaRepository extends JpaRepository<Flashcard, Long> {
  Optional<Flashcard> findById(long id);
  List<Flashcard> findByFlashcardSet(FlashcardSet flashcardSet);
}
