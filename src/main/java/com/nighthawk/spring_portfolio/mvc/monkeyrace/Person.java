package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import javax.persistence.Entity;

import lombok.*;

@Data
@Entity
public class Person extends DataObject implements DataObject.InterfaceToJson {
	// Class data

	// Instance data
	private String name;
    private String email;
    private String passwordHash;

	/* constructor
	 *
	 */
	public Person(String name, String email, String passwordHash)
	{
		super("Person");
		this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
	}


	
	/* 'Generics' requires toString override
	 * toString provides data based off of Static Key setting
	 */
	@Override
	public String toString()
	{
		return "Name: " + name + ", Email: " + email + ", Password: " + passwordHash;
	}

	/* 'Generics' requires toJson override
	 * toJson provides data based off of Static Key setting
	 */
	@Override
	public String toJson()
	{
		
		// return all data in json format
		return "{ \"Name\": \"" + name + "\", \"Email\": \"" + email + "\", \"Password\": \"" + passwordHash + "\" }";
	}

	// Test data initializer
	public static Person[] persons()
    {
        return new Person[] {
            new Person("John", "john@email.com", "password"),
            new Person("Mary", "mary@email.com", "password2"),
            new Person("Bob", "bob@email.com", "password3")
        };
    }
	
	/* main to test Person class
	 * 
	 */
	public static void main(String[] args)
	{
		// Inheritance Hierarchy
		//Person[] objs = persons();
		Person john = new Person("John", "john@email.com", "password");
		// print with title
		System.out.println("Person Class Test");
		System.out.println(john.toString());
		System.out.println(john.toJson());

		// test with parent class
		DataObject mary = new Person("Mary", "mary@email.com", "password2");
		System.out.println(mary.toString());
		//System.out.println(mary.toJson());
		// throws error because toJson is not defined in DataObject
	}

}