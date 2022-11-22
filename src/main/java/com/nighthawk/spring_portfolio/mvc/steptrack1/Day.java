package com.nighthawk.spring_portfolio.mvc.steptrack1;

import javax.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Day {
    // attributes, how would you track days.  perhaps capture date and have many method to calculate this data
    private int year;
    private int day;
    private int steps;
 
    // relationship
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;
}
