package com.nighthawk.spring_portfolio.mvc.frqs.steptrack1;

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

    public static void main(String[] args) {
      Day day = new Day();
      day.setCalories(100);
      day.setDay(10);
      day.setDistanceMiles(1.1);
      day.setId(null);
      day.setMonth(5);
      day.setSteps(10000);
      day.setYear(2022);
      System.out.println(day);

      Day day2 = new Day(null, 2022, 5, 10, 10000, 100, 1.1);
      System.out.println(day2);
    }
  }