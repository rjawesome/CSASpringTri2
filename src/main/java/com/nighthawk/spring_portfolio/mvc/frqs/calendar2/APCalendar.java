package com.nighthawk.spring_portfolio.mvc.frqs.calendar2;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
// Prototype Implementation
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class APCalendar {

    /** Returns true if year is a leap year and false otherwise.
     * isLeapYear(2019) returns False
     * isLeapYear(2016) returns True
     */          
    public static boolean isLeapYear(int year) {
        if (year % 4 != 0) {
          return false;
        } else if (year % 400 == 0) {
          return true;
        } else if (year % 100 == 0) {
          return false;
        } else {
          return true;
        }
      }
        
    /** Returns the value representing the day of the week 
     * 0 denotes Sunday, 
     * 1 denotes Monday, ..., 
     * 6 denotes Saturday. 
     * firstDayOfYear(2019) returns 2 for Tuesday.
    */
    public static int firstDayOfYear(int year) {
        // implementation not shown
        int firstDayTest = (int) (Math.random()*7);
        return firstDayTest;
        }


    /** Returns n, where month, day, and year specify the nth day of the year.
     * This method accounts for whether year is a leap year. 
     * dayOfYear(1, 1, 2019) return 1
     * dayOfYear(3, 1, 2017) returns 60, since 2017 is not a leap year
     * dayOfYear(3, 1, 2016) returns 61, since 2016 is a leap year. 
    */ 
    public static int dayOfYear(int month, int day, int year) {
        // implementation not shown

        int dayOfYearTest = (int) (Math.random()*365);
        return dayOfYearTest;
        }

    /** Returns the number of leap years between year1 and year2, inclusive.
     * Precondition: 0 <= year1 <= year2
    */ 
    public static int numberOfLeapYears(int year1, int year2) {
          int leapYearCount = 0; // variable to keep track of amount of leap years

          // for loop: set year to year1, and while it is not yet at year2, find out if leapyear or not
          for(int year = year1; year <= year2; year++) {
      
              // check the year for if it is leap year
              if(isLeapYear(year)) {
                  leapYearCount++; // increase count
              }
          }
          return leapYearCount; // return the integer for the count of leap years
        }

    /** Returns the value representing the day of the week for the given date
     * Precondition: The date represented by month, day, year is a valid date.
    */
    public static int dayOfWeek(int month, int day, int year) { 
        // to be implemented in part (b)
        int firstDay = firstDayOfYear(year); // store the day of the week of the first day
        int dayAfterNew = dayOfYear(month, day, year); // store the number of days since new year (inclusive)

        return (firstDay + dayAfterNew - 1) % 7; // adds the day of the week to the days since new year, but minus 1 because the dayAfterNew includes the first day. then, mod 7
        }

    public static String yearFact(int year) throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://numbersapi.p.rapidapi.com/" + year + "/year?fragment=true&json=true"))
          .header("X-RapidAPI-Key", "0fb4bb4059msh8489d467f79e14ep1d57f9jsnd0d67fb163e6")
          .header("X-RapidAPI-Host", "numbersapi.p.rapidapi.com")
          .method("GET", HttpRequest.BodyPublishers.noBody())
          .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      
        String responseBody = response.body();
        Object obj = new JSONParser().parse(responseBody);
        JSONObject jsonObj = (JSONObject) obj;

        String fact = (String) jsonObj.get("text");

        return fact;
    }

    /** Tester method */
    public static void main(String[] args) {
        // Private access modifiers
        System.out.println("firstDayOfYear: " + APCalendar.firstDayOfYear(2022));
        System.out.println("dayOfYear: " + APCalendar.dayOfYear(1, 1, 2022));

        // Public access modifiers
        System.out.println("isLeapYear: " + APCalendar.isLeapYear(2022));
        System.out.println("numberOfLeapYears: " + APCalendar.numberOfLeapYears(2000, 2022));
        System.out.println("dayOfWeek: " + APCalendar.dayOfWeek(1, 1, 2022));
    }

}