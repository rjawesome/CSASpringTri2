package com.nighthawk.spring_portfolio.mvc.frqs.calendar1;

/** Simple POJO 
 * Used to Interface with APCalendar
 * The toString method(s) prepares object for JSON serialization
 * Note... this is NOT an entity, just an abstraction
 */
public class Year {
  private int year;
  private boolean isLeapYear;
  private int firstDayOfYear;
  private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

  // zero argument constructor
  public Year() {} 

  /* year getter/setters */
  public int getYear() {
     return year;
  }
  public void setYear(int year) {
     this.year = year;
     this.setIsLeapYear(year);
     this.setFirstDayOfYear(year);
  }

  /* isLeapYear getter/setters */
  public boolean getIsLeapYear(int year) {
     return APCalendar.isLeapYear(year);
  }
  private void setIsLeapYear(int year) {  // this is private to avoid tampering
     this.isLeapYear = APCalendar.isLeapYear(year);
  }

    /* isLeapYear getter/setters */
    public int getFirstDayOfYear(int year) {
      return APCalendar.firstDayOfYear(year);
   }
   private void setFirstDayOfYear(int year) {  // this is private to avoid tampering
      this.firstDayOfYear = APCalendar.firstDayOfYear(year);
   }

  /* isLeapYearToString formatted to be mapped to JSON */
  public String toJSON(){
     return ( "{ \"year\": "  +this.year+  ", " + "\"isLeapYear\": "  +this.isLeapYear + ", \"firstDayOfYear\": \"" + days[this.firstDayOfYear] +  "\" }" );
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
