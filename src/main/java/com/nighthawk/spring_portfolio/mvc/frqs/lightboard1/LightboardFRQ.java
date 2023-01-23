package com.nighthawk.spring_portfolio.mvc.frqs.lightboard1;

public class LightboardFRQ {
    private Light1[][] lights;
    public LightboardFRQ(LightBoard1 board) {
        lights = board.getLights();
    } 

    public boolean evaluateLight(int row, int col) {
        int onInColumn = 0;
        
        for(int r = 0; r < lights.length; r++)
            if(lights[r][col].on)
                onInColumn++;
                
        if(lights[row][col].on) {
            if(onInColumn % 2 == 0)
                return false;
        }
        else {
            if(onInColumn % 3 == 0)
                return true;
        } 
        return lights[row][col].on;
    }
}