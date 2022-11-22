package com.nighthawk.spring_portfolio.mvc.steptrack1;

import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Day {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // attributes, how would you track days.  perhaps capture date and have many method to calculate this data
    private int year;
    private int month;
    private int day;
    private int steps;
    private int calories;
    private double distanceMiles;
 
    // relationship
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;
}
