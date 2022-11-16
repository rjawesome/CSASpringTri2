package com.nighthawk.spring_portfolio.mvc.calendar1;

import org.springframework.data.mongodb.core.aggregation.DateOperators.DayOfYear;

public class Day {
    private int year;
    private int month;
    private int day;
    private int dayOfYear;
    private int dayOfWeek;
    private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
  
    // zero argument constructor
    public Day(int day, int month, int year) {
        this.year = year;
        this.month = month;
        this.day = day;
    } 

  
    /* year getter/setters */
    public int getYear() {
       return year;
    }
    public void setYear(int year) {

    }
  
    /* dayOfYear getter/setters */
    public int getDayOfYear() {
       return APCalendar.dayOfYear(month, day, year);
    }
    private void setDayOfYear() {  // this is private to avoid tampering
       this.dayOfYear = APCalendar.dayOfYear(month, day, year);
    }

    /* dayOfWeek getter/setters */
    public int getDayOfWeek() {
        return APCalendar.dayOfWeek(month, day, year);
    }
    private void setDayOfWeek() {  // this is private to avoid tampering
        this.dayOfYear = APCalendar.dayOfWeek(month, day, year);
    }
  
    /* isLeapYearToString formatted to be mapped to JSON */
    public String toJSON(){
        return String.format("{ \"year\": %d, \"month\": %d, \"day\": %d, \"dayOfWeek\": %s, \"dayOfYear\": %d}", this.year, this.month, this.day, this.dayOfWeek, this.dayOfYear);
    }	
  
    /* standard toString placeholder until class is extended */
    public String toString() { 
       return toJSON(); 
    }
  
    public static void main(String[] args) {
       Year year = new Year();
       year.setYear(2022);
       System.out.println(year);
    }
  }