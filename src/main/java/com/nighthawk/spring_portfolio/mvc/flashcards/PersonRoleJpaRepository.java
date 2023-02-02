package com.nighthawk.spring_portfolio.mvc.flashcards;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRoleJpaRepository extends JpaRepository<PersonRole, Long> {
    PersonRole findByName(String name);
}
