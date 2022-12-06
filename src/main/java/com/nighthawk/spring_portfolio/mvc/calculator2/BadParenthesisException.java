package com.nighthawk.spring_portfolio.mvc.calculator2;

public class BadParenthesisException extends Exception {
    public BadParenthesisException() {
        super("not complete parenthesis pairs");
    }
    
}
