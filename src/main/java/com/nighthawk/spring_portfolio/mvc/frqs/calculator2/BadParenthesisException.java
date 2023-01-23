package com.nighthawk.spring_portfolio.mvc.frqs.calculator2;

public class BadParenthesisException extends Exception {
    public BadParenthesisException() {
        super("not complete parenthesis pairs");
    }
    
}
