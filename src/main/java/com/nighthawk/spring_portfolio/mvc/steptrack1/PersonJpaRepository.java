package com.nighthawk.spring_portfolio.mvc.steptrack1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface PersonJpaRepository extends JpaRepository<Person, Long> {
    Optional <Person> findByEmail(String email);
}
