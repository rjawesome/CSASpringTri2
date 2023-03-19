package com.nighthawk.spring_portfolio.mvc.monkeyrace;

public class Person extends Generics {
	// Class data
	public static KeyTypes key = KeyType.title;  // static initializer
	public static void setOrder(KeyTypes key) { Person.key = key; }
	public enum KeyType implements KeyTypes {title, name, email, passwordHash}

	// Instance data
	private final String name;
    private final String email;
    private final String passwordHash;

	/* constructor
	 *
	 */
	public Person(String name, String email, String passwordHash)
	{
		super.setType("Person");
		this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
	}

	/* 'Generics' requires getKey to help enforce KeyTypes usage */
	@Override
	protected KeyTypes getKey() { return Person.key; }
	
	/* 'Generics' requires toString override
	 * toString provides data based off of Static Key setting
	 */
	@Override
	public String toString()
	{
		String output="";
		if (KeyType.name.equals(this.getKey())) {
			output += this.name;
		} else if (KeyType.email.equals(this.getKey())) {
			output += "00" + this.email;
			output = output.substring(output.length() - 2);
		} else if (KeyType.passwordHash.equals(this.getKey())) {
			output += this.passwordHash;
		} else {
			output += super.getType() + ": " + this.name + ", " + this.email + ", " + this.passwordHash;
		}
		return output;
		
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
		Person[] objs = persons();

		// print with title
		Person.setOrder(KeyType.title);
		Person.print(objs);

		// print name only
		Person.setOrder(KeyType.name);
		Person.print(objs);
	}

}