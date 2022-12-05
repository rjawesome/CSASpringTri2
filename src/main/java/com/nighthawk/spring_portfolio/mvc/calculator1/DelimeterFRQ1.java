package com.nighthawk.spring_portfolio.mvc.calculator1;

import java.util.ArrayList;

public class DelimeterFRQ1 {
    private char openDel;
    private char closeDel;
    
    public DelimeterFRQ1 (char o, char c) {
        this.openDel = o;
        this.closeDel = c;
    }
    
    public ArrayList<String> getDelimitersList(String input) {
        ArrayList<String> delimeter = new ArrayList<String>();
        for (char each : input.toCharArray()) {
            if (each.equals(closeDel) || each.equals(openDel)) {
                delimeter.add(each);
            }
        }
        return delimeter;
    }
    public boolean isBalanced(ArrayList<String> delimiters) {
        int openDels = 0;
        int closeDels = 0;

        for (String each : delimiters) {
            if (each.equals(openDel)) {
                openDels++;
            }
            else {
                closeDels++;
            }
            if (closeDels > openDels) {
                return false;
            }
        }
        if (openDels == closeDels) {
            return true;
        }
        else {
            return false;
        }
    }
}
