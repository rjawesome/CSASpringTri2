package com.nighthawk.spring_portfolio.mvc.monkeyrace;
public abstract class Generics {
	public final String masterType = "Generic";
	private String type;	// extender should define their data type

	// generic enumerated interface
	public interface KeyTypes {
		String name();
	}
	protected abstract KeyTypes getKey();  	// this method helps force usage of KeyTypes

	// getter
	public String getMasterType() {
		return masterType;
	}

	// getter
	public String getType() {
		return type;
	}

	// setter
	public void setType(String type) {
		this.type = type;
	}
	
	// this method is used to establish key order
	public abstract String toString();

	// static print method used by extended classes
	public static void print(Generics[] objs) {
		// print 'Object' properties
		System.out.println(objs.getClass() + " " + objs.length);

		// print 'Generics' properties
		if (objs.length > 0) {
			Generics obj = objs[0];	// Look at properties of 1st element
			System.out.println(
					obj.getMasterType() + ": " + 
					obj.getType() +
					" listed by " +
					obj.getKey());
		}

		// print "Generics: Objects'
		for(Object o : objs)	// observe that type is Opaque
			System.out.println(o);

		System.out.println();
	}
}