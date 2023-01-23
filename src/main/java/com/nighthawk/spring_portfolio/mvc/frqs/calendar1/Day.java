package com.nighthawk.spring_portfolio.mvc.frqs.calendar1;

import java.io.IOException;

import org.json.simple.parser.ParseException;

public class Day {
    private int year;
    private int month;
    private int day;
    private int dayOfYear;
    private int dayOfWeek;
    private String birthday;
    private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
  
   // zero argument constructor
   public Day() {

   }
   // all argument constructor
   public Day(int day, int month, int year) throws IOException, InterruptedException, ParseException {
      this.year = year;
      this.month = month;
      this.day = day;
      this.setDayOfWeek();
      this.setDayOfYear();
      this.setBirthday();
   } 

  
   /* year getter/setters */
   public int getYear() {
      return year;
   }
   public void setYear(int year) {
     this.year = year;
   }

   /* month getter/setters */
   public int getMonth() {
      return month;
   }
   public void setMonth(int month) {
      this.month = month;
   }

   /* day getter/setters */
   public int getDay() {
      return day;
   }
   public void setDay(int day) {
      this.day = day;
   }
  
   /* dayOfYear getter/setters */
   public int getDayOfYear() {
      return APCalendar.dayOfYear(month, day, year);
   }
   private void setDayOfYear() {  // this is private to avoid tampering
      this.dayOfYear = APCalendar.dayOfYear(month, day, year);
   }

   /* dayOfYear getter/setters */
   public String getBirthday() throws IOException, InterruptedException, ParseException {
      return APCalendar.getBirthdays(month, day, year);
   }
   private void setBirthday() throws IOException, InterruptedException, ParseException {  // this is private to avoid tampering
      this.birthday = APCalendar.getBirthdays(month, day, year); 
   }

    /* dayOfWeek getter/setters */
    public int getDayOfWeek() {
        return APCalendar.dayOfWeek(month, day, year);
    }
    private void setDayOfWeek() {  // this is private to avoid tampering
        this.dayOfWeek = APCalendar.dayOfWeek(month, day, year);
    }
  
    /* isLeapYearToString formatted to be mapped to JSON */
    public String toJSON(){
        return String.format("{ \"year\": %d, \"month\": %d, \"day\": %d, \"dayOfWeek\": \"%s\", \"dayOfYear\": %d, \"famousBirthday\": \"%s\"}", this.year, this.month, this.day, this.days[dayOfWeek], this.dayOfYear, this.birthday);
    }	
  
    /* standard toString placeholder until class is extended */
    public String toString() { 
       return toJSON(); 
    }
  
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
       Day day = new Day(1, 2, 2000);
       System.out.println(day);
    }
  }