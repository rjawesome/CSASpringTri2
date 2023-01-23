package com.nighthawk.spring_portfolio.mvc.frqs.calendar2;

import java.io.IOException;

import org.json.simple.parser.ParseException;

/** Simple POJO 
 * Used to Interface with APCalendar
 * The toString method(s) prepares object for JSON serialization
 * Note... this is NOT an entity, just an abstraction
 */
class Year {
   private int year;
   private String yearFact;
   private boolean isLeapYear;
   private int firstDayOfYear;

   // zero argument constructor
   public Year() {} 

   /* year getter/setters */
   public int getYear() {
      return year;
   }
   public void setYear(int year) throws IOException, InterruptedException, ParseException {
      this.year = year;
      this.setIsLeapYear(year);
      this.setFirstDayOfYear(year);
      this.setYearFact(year);
   }

   /* isLeapYear getter/setters */
   public boolean getIsLeapYear(int year) {
      return APCalendar.isLeapYear(year);
   }
   private void setIsLeapYear(int year) {  // this is private to avoid tampering
      this.isLeapYear = APCalendar.isLeapYear(year);
   }

   public int getFirstDayOfYear(int year) {
      return APCalendar.firstDayOfYear(year);
   }
   private void setFirstDayOfYear(int year) {  // this is private to avoid tampering
      this.firstDayOfYear = APCalendar.firstDayOfYear(year);
   }

   private void setYearFact(int year) throws IOException, InterruptedException, ParseException {  // this is private to avoid tampering
      this.yearFact = APCalendar.yearFact(year);
   }

   /* isLeapYearToString formatted to be mapped to JSON */
   public String isLeapYearToString(){
      return ( "{ \"year\": "  +this.year+  ", " + "\"isLeapYear\": "  +this.isLeapYear+ " }" );
   }
   	
   public String firstDayOfYearToString(){
      return ( "{ \"year\": "  +this.year+  ", " +  "\"firstDayOfYear\": "  +this.firstDayOfYear + " }" );
   }

   public String yearFactToString(){
      return ( "{ \"year\": "  +this.year+  ", " +  "\"yearFact\": " + "\"" + this.yearFact + "\"" + " }" );
   }

   /* standard toString placeholder until class is extended */
   public String toString() { 
      return isLeapYearToString(); 
   }

   public static void main(String[] args) throws IOException, InterruptedException, ParseException {
      Year year = new Year();
      year.setYear(2022);
      System.out.println(year);
   }
}