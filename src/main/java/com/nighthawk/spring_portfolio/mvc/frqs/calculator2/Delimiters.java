package com.nighthawk.spring_portfolio.mvc.frqs.calculator2;

import java.util.ArrayList;

public class Delimiters {
    private String openDel;
    private String closeDel;

    public Delimiters(String open, String close) {
        openDel = open;
        closeDel = close;
    }

    public ArrayList<String> getDelimitersList(String[] tokens) {
        ArrayList<String> delimList = new ArrayList<String>();
        for (String i : tokens ) {
            if (i.equals(openDel) || i.equals(closeDel)) {
                delimList.add(i);
            }
        }
        return delimList;
    }

    public boolean isBalanced(ArrayList<String> delimiters) {
        int open = 0;
        int close = 0;
        for (String delim : delimiters) {
            if (delim.equals(openDel)) {
                open++;
            } else {
                close++;
            }
        }

        if (open == close) {
            return true;
        } else return false;
    }
}
