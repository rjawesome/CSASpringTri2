package com.nighthawk.spring_portfolio.mvc.frqs.steptrack1;
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
public class Person1 {
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
    int activeSteps;
    String passwordHash;

    // relationship
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true, 
        fetch = FetchType.EAGER
    )
    private List<Day> days = new ArrayList<>();

    public void addDay(Day day) {
        days.add(day);
    }

    public int getActiveDays() {
      int activeDays = 0;
      for (Day day : days) {
        if (day.getSteps() >= activeSteps) {
          activeDays++;
        }
      }
      return activeDays;
    }

    public double getAverageSteps() {
      double totalSteps = 0;
      for (Day day : days) {
        totalSteps += day.getSteps();
      }
      return totalSteps/days.size();
    }

    public static void main(String[] args) {
      Person1 person = new Person1();
      person.setActiveSteps(1000);
      person.setAge(16);
      person.setGender("Male");
      person.setDays(null);
      person.setEmail("rohanj2006@gmail.com");
      person.setHeightIn(65);
      person.setId(null);
      person.setName("Rohan Juneja");
      person.setPasswordHash("asodjaosidjoa");
      person.setWeightLbs(120);
      System.out.println(person);

      Person1 person2 = new Person1(null, "Rohan", "rohanj2006@gmail.com", "Female", 16, 65, 120, 10000, "asdjaoi", null);
      System.out.println(person2);
    }
}
