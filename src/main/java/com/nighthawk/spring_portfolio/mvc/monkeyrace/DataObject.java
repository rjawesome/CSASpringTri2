package com.nighthawk.spring_portfolio.mvc.monkeyrace;
public abstract class DataObject {
    // Class data
    private String type;
    
    // Constructor
    public DataObject(String type) {
        this.type = type;
    }
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public interface InterfaceToJson {
        String toJson();
    }
    
    // toString default
    @Override
    public String toString() {
        return "Type: " + type;
    }
}