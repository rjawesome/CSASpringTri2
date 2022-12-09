package com.nighthawk.spring_portfolio.mvc.lightboard2;

import java.util.Scanner;
import java.awt.Color;

import lombok.Data;

@Data  // Annotations to simplify writing code (ie constructors, setters)
public class LightBoard {
    private Light[][] lights;
    boolean input = false;

    /* Initialize LightBoard and Lights */
    public LightBoard(int numRows, int numCols, boolean input) {
        this.lights = new Light[numRows][numCols];
        Scanner getInput = new Scanner(System.in);
        String hexCode;
        short effectInput;
        // 2D array nested loops, used for initialization
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (input) {
                    System.out.println("Enter hexadecimal: ");
                    hexCode = getInput.nextLine();
                    System.out.println("Enter effect: ");
                    effectInput = getInput.nextShort();
                    getInput.nextLine(); // This line has to be added as buffer or else terminal will skip over next reading
                    short[] rgbList = hexToRGB(hexCode);
                    lights[row][col] = new Light(rgbList[0], rgbList[1], rgbList[2], effectInput);
                } else {
                    lights[row][col] = new Light(); // each cell needs to be constructed
                }  
            }
        }
        getInput.close();
    }

    public boolean evaluateLight(int row, int col) {
        int numOn = 0;
        for (int r = 0; r < lights.length; r++) {
            if (lights[r][col].effect == 0) {
                numOn++;
            }
        }
        if (lights[row][col].effect == 0 && numOn % 2 == 0) {
            return false;
        }
        if (lights[row][col].effect != 0 && numOn % 3 == 0) {
            return true;
        }
        return lights[row][col].effect == 0;
    }

    public LightBoard(int numRows, int numCols) {
        this(numRows, numCols, false);
    }
    /* Output is intended for API key/values */
    public String toString() { 
        String outString = "[";
        // 2D array nested loops, used for reference
        for (int row = 0; row < lights.length; row++) {
            for (int col = 0; col < lights[row].length; col++) {
                outString += 
                // data
                "{" + 
                "\"row\": " + row + "," +
                "\"column\": " + col + "," +
                "\"light\": " + lights[row][col] +   // extract toString data
                "}," ;
            }
        }
        // remove last comma, newline, add square bracket, reset color
        outString = outString.substring(0,outString.length() - 1) + "]";
		return outString;
    }

    /* Output is intended for Terminal, effects added to output */
    public String toTerminal() { 
        String outString = "[";
        // 2D array nested loops, used for reference
        for (int row = 0; row < lights.length; row++) {
            for (int col = 0; col < lights[row].length; col++) {
                outString += 
                // reset
                "\033[m" +
                
                // color
                "\033[38;2;" + 
                lights[row][col].getRed() + ";" +  // set color using getters
                lights[row][col].getGreen() + ";" +
                lights[row][col].getBlue() + ";" +
                lights[row][col].getEffect() + "m" +
                // data, extract custom getters
                "{" +
                "\"" + "RGB\": " + "\"" + lights[row][col].getRGB() + "\"" +
                "," +
                "\"" + "Effect\": " + "\"" + lights[row][col].getEffectTitle() + "\"" +
                "}," +
                // newline
                "\n" ;
            }
        }
        // remove last comma, newline, add square bracket, reset color
        outString = outString.substring(0,outString.length() - 2) + "\033[m" + "]";
		return outString;
    }

    /* Output is intended for Terminal, draws color palette */
    public String toColorPalette() {
        // block sizes
        final int ROWS = 5;
        final int COLS = 10;

        // Build large string for entire color palette
        String outString = "";
        // find each row
        for (int row = 0; row < lights.length; row++) {
            // repeat each row for block size
            for (int i = 0; i < ROWS; i++) {
                // find each column
                for (int col = 0; col < lights[row].length; col++) {
                    // repeat each column for block size
                    for (int j = 0; j < COLS; j++) {
                        // print single character, except at midpoint print color code
                        String c = (i == (int) (ROWS / 2) && j == (int) (COLS / 2) ) 
                            ? lights[row][col].getRGB()
                            : (j == (int) (COLS / 2))  // nested ternary
                            ? " ".repeat(lights[row][col].getRGB().length())
                            : " ";

                        outString += 
                        // reset
                        "\033[m" +
                        
                        // color
                        "\033[38;2;" + 
                        lights[row][col].getRed() + ";" +
                        lights[row][col].getGreen() + ";" +
                        lights[row][col].getBlue() + ";" +
                        "7m" +

                        // color code or blank character
                        c +

                        // reset
                        "\033[m";
                    }
                }
                outString += "\n";
            }
        }
        // remove last comma, newline, add square bracket, reset color
        outString += "\033[m";
		return outString;
    }
    
    public short[] hexToRGB(String hexCode) {
        Color rgbValues = Color.decode(hexCode);

        return new short[]{(short)rgbValues.getRed(), (short)rgbValues.getGreen(), (short)rgbValues.getBlue()};

    }
    static public void main(String[] args) {
        // create and display LightBoard
        LightBoard lightBoard = new LightBoard(3, 3);
        System.out.println(lightBoard);  // use toString() method
        System.out.println(lightBoard.toTerminal());
        System.out.println(lightBoard.toColorPalette());
    }
}