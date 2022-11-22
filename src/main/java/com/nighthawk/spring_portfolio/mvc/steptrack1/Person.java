package com.nighthawk.spring_portfolio.mvc.steptrack1;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // Person data
    String name;
    @NotEmpty
    @Size(min=5)
    @Column(unique=true)
    @Email
    String email;
    String gender;
    int age;
    int heightIn;
    int weightLbs;
    String passwordHash;

    // relationship
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Day> days = new ArrayList<>();
}
