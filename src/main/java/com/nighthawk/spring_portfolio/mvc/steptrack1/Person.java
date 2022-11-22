package com.nighthawk.spring_portfolio.mvc.steptrack1;

import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    // Person data
    String name;
    String email;

    // relationship
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Day> days = new ArrayList<>();
}
