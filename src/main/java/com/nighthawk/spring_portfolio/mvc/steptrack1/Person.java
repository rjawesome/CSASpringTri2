package com.nighthawk.spring_portfolio.mvc.steptrack1;

import javax.persistence.*;

@Entity
public class Person {
    // Person data

    // relationship
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Day> days = new ArrayList<>();
}
