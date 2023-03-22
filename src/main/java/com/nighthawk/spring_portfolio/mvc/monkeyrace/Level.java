package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import javax.persistence.Entity;

import lombok.*;

@Data
@Entity
public class Level extends DataObject implements DataObject.InterfaceToJson {
	// Class data

	// Instance data
	private String name;
  private String question;
  private String answer;

	/* constructor
	 *
	 */
	public Level(String name, String question, String answer)
	{
		super("Level");
		this.name = name;
    this.question = question;
    this.answer = answer;
	}


	
	/* 'Generics' requires toString override
	 * toString provides data based off of Static Key setting
	 */
	@Override
	public String toString()
	{
		return "Name: " + name + ", Question: " + question + ", Answer: " + answer;
	}

	/* 'Generics' requires toJson override
	 * toJson provides data based off of Static Key setting
	 */
	@Override
	public String toJson()
	{
		
		// return all data in json format
		return "{ \"Name\": \"" + name + "\", \"Question\": \"" + question + "\", \"Answer\": \"" + answer + "\" }";
	}

	// Test data initializer
	public static Level[] levels()
    {
        return new Level[] {
            new Level("Bubble Sort", "What is the complexity of bubble sort?", "O(n^2)"),
            new Level("Merge Sort", "What is the complexity of merge sort?", "O(nlogn)"),
            new Level("Quick Sort", "What is the amortized complexity of quick sort?", "O(nlogn)")
        };
    }
	
	/* main to test Level class
	 * 
	 */
	public static void main(String[] args)
	{
		// Inheritance Hierarchy
		Level[] objs = levels();
		// print with title
		System.out.println("Level Class Test");
		System.out.println(objs[0].toString());
		System.out.println(objs[0].toJson());

		// test with parent class
		DataObject level2 = objs[1];
		System.out.println(level2.toString());
		//System.out.println(level2.toJson());
		// throws error because toJson is not defined in DataObject
	}

}
