package com.nighthawk.spring_portfolio.mvc.calculator1;

import java.util.ArrayList;

public class DelimeterFRQ1 {
    private String openDel;
    private String closeDel;
    public ArrayList<String> getDelimitersList(String[] tokens) {
        ArrayList<String> delimeter = new ArrayList<String>();
        for (String each : tokens) {
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