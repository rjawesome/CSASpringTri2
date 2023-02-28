package com.nighthawk.spring_portfolio.mvc.flashcards;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface StatsJpaRepository extends JpaRepository<Stats, Long> {
  Optional<Stats> findById(long id);
  List<Stats> findByFlashcardSetId(long flashcardSetId);
  List<Stats> findByUserAndFlashcardSet(Person user, FlashcardSet set);
  List<Stats> findByUserAndFlashcard(Person user, Flashcard flashcard);
}

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
