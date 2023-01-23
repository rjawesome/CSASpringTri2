package com.nighthawk.spring_portfolio.mvc.frqs.calendar1;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class APCalendar {
    private static int firstDayOf1970 = 4;
    private static String[] months = {"", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
    /** Returns true if year is a leap year and false otherwise.
     * isLeapYear(2019) returns False
     * isLeapYear(2016) returns True
     */          
    public static boolean isLeapYear(int year) {
        if (year % 400 == 0) return true;
        if (year % 100 == 0) return false;
        if (year % 4 == 0) return true;
        return false;
    }
        
    /** Returns the value representing the day of the week 
     * 0 denotes Sunday, 
     * 1 denotes Monday, ..., 
     * 6 denotes Saturday. 
     * firstDayOfYear(2019) returns 2 for Tuesday.
    */
    public static int firstDayOfYear(int year) {
        int day = firstDayOf1970;
        for (int i = 1970; i<year; i++) {
            if (isLeapYear(i)) day = (day + (366 % 7)) % 7;
            else day = (day + (365 % 7)) % 7;
        }
  
        return day;
    }
  
  
    /** Returns n, where month, day, and year specify the nth day of the year.
     * This method accounts for whether year is a leap year. 
     * dayOfYear(1, 1, 2019) return 1
     * dayOfYear(3, 1, 2017) returns 60, since 2017 is not a leap year
     * dayOfYear(3, 1, 2016) returns 61, since 2016 is a leap year. 
    */ 
    public static int dayOfYear(int month, int day, int year) {
        // base case
        if (month == 1) {
            return Math.min(day, 31);
        }
        
        // february
        if (month == 2) {
            return Math.min(day, 28 + (isLeapYear(year) ? 1 : 0)) + dayOfYear(month-1, 50, year);
        }
        
        // 31 day months
        if (month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11) {
            return Math.min(day, 31) + dayOfYear(month-1, 50, year);
        }
        
        // 30 day months
        return Math.min(day, 30) + dayOfYear(month-1, 50, year);
    }
  
    /** Returns the number of leap years between year1 and year2, inclusive.
     * Precondition: 0 <= year1 <= year2
    */ 
    public static int numberOfLeapYears(int year1, int year2) {
        int LeapYearAmount = 0;  // defines the variable we're using to count the leap year amount
        for (int year = year1; year <= year2; year++) { // for each year between year 1 and year 2
            if (isLeapYear(year)) { // if the year is a leap year, add one to the count
                LeapYearAmount+=1;
            }
        }
        return LeapYearAmount;
    }
  
    /** Returns the value representing the day of the week for the given date
     * Precondition: The date represented by month, day, year is a valid date.
    */
    public static int dayOfWeek(int month, int day, int year) { 
        int DayFirst = firstDayOfYear(year); // finds what the first day of the year is for the given year
        int DaysAfter = dayOfYear(month, day, year); // finds how many days after the start of the year
        int CalculatedDay = (DayFirst + DaysAfter - 1); // adds the amount to the first day and subtracts 1
        int DayWeekReturn = CalculatedDay % 7; // changes from a number to a value 0-6 that gives the day of the week
        return DayWeekReturn; // returns the day of the week
    }

    public static String getBirthdays(int month, int day, int year) throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("https://birthdays.rohanj.dev/api/date/%d/%s", day, months[month])))
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject famousbirthdays = (JSONObject) new JSONParser().parse(response.body());
        JSONArray birthdaysarray = (JSONArray) famousbirthdays.get("data");
        JSONObject birthday = (JSONObject) birthdaysarray.get(0);
        return (String) birthday.get("name");
    }
  
    /** Tester method */
    public static void main(String[] args) {
        // Private access modifiers
        System.out.println("isLeapYear (2022): " + APCalendar.isLeapYear(2022));
        System.out.println("firstDayOfYear (2022): " + APCalendar.firstDayOfYear(2022));
        System.out.println("firstDayOfYear (2019): " + APCalendar.firstDayOfYear(2019));
        System.out.println("dayOfYear (1/1/2022): " + APCalendar.dayOfYear(1, 1, 2022));
        System.out.println("dayOfYear (3/1/2016): " + APCalendar.dayOfYear(3, 1, 2016));
        System.out.println("dayOfYear (3/1/2017): " + APCalendar.dayOfYear(3, 1, 2017));
  
        // Public access modifiers
        System.out.println("numberOfLeapYears (2000-2022): " + APCalendar.numberOfLeapYears(2000, 2022));
        System.out.println("dayOfWeek (1/1/2022): " + APCalendar.dayOfWeek(1, 1, 2022));
        System.out.println("dayOfWeek (1/10/2019): " + APCalendar.dayOfWeek(1, 10, 2019));
    }
  
  }