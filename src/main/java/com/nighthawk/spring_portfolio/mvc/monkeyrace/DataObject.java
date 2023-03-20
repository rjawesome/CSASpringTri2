package com.nighthawk.spring_portfolio.mvc.monkeyrace;
public abstract class DataObject {
    private String type;
    
    public DataObject(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "Type: " + type;
    }
}