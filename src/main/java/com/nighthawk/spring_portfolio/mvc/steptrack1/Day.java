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

    public void appendSteps (int steps) {
      this.steps += steps;
    }

    public void appendCalories (int calories) {
      this.calories += calories;
    }

    public void appendDistance (double miles) {
      this.distanceMiles += miles;
    }

    public boolean isDate (int day, int month, int year) {
      return this.day == day && this.month == month && this.year == year;
    }
}
