package com.nighthawk.spring_portfolio.mvc.frqs.lightboard2;

import java.util.Scanner;
import java.awt.Color;

import lombok.Data;

@Data  // Annotations to simplify writing code (ie constructors, setters)
public class LightBoard {
    private Light[][] lights;
    boolean input = false;

    /* Initialize LightBoard and Lights */
    public LightBoard(int numRows, int numCols, boolean input, String pattern) {
        this.lights = new Light[numRows][numCols];
        Scanner getInput = new Scanner(System.in);
        // 2D array nested loops, used for initialization

        // Check for pattern
        switch (pattern) {
            case "checkerboard":
                System.out.println("Enter hexadecimal 1: ");
                String hexCode1 = getInput.nextLine();
                System.out.println("Enter effect 1: ");
                short effectInput1 = getInput.nextShort();
                getInput.nextLine(); // This line has to be added as buffer or else terminal will skip over next reading
                short[] rgbList1 = hexToRGB(hexCode1);
                System.out.println("Enter hexadecimal 2: ");
                String hexCode2 = getInput.nextLine();
                System.out.println("Enter effect 2: ");
                short effectInput2 = getInput.nextShort();
                getInput.nextLine(); // This line has to be added as buffer or else terminal will skip over next reading
                short[] rgbList2 = hexToRGB(hexCode2);

                boolean alternate = true;
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        if (alternate) {
                            lights[row][col] = new Light(rgbList1[0], rgbList1[1], rgbList1[2], effectInput1);
                            if (numCols % 2 == 0 && col == numCols - 1) { // Skip over if at the end of an even number of columns otherwise the pattern won't match up
                                continue;
                            }
                            alternate = false;
                        } else {
                            lights[row][col] = new Light(rgbList2[0], rgbList2[1], rgbList2[2], effectInput2);
                            if (numCols % 2 == 0 && col == numCols - 1) { // Skip over if at the end of an even number of columns otherwise the pattern won't match up
                                continue;
                            }
                            alternate = true;
                        }
                    }
                }
                break;
            case "frame":
                // Just to be clear, O stands for outer frame and I stands for inner picture
                System.out.println("Enter hexadecimal 1: ");
                String hexCodeO = getInput.nextLine();
                System.out.println("Enter effect 1: ");
                short effectInputO = getInput.nextShort();
                getInput.nextLine(); // This line has to be added as buffer or else terminal will skip over next reading
                short[] rgbListO = hexToRGB(hexCodeO);
                System.out.println("Enter hexadecimal 2: ");
                String hexCodeI = getInput.nextLine();
                System.out.println("Enter effect 2: ");
                short effectInputI = getInput.nextShort();
                getInput.nextLine(); // This line has to be added as buffer or else terminal will skip over next reading
                short[] rgbListI = hexToRGB(hexCodeI);

                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        if (row == 0 || row == numRows - 1 || col == 0 || col == numCols - 1) {
                            lights[row][col] = new Light(rgbListO[0], rgbListO[1], rgbListO[2], effectInputO);
                        } else {
                            lights[row][col] = new Light(rgbListI[0], rgbListI[1], rgbListI[2], effectInputI);
                        }
                    }
                }
                break;
            default:
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        // Getting input only if input == true
                        if (input) {
                            System.out.println("Enter hexadecimal: ");
                            String hexCode = getInput.nextLine();
                            System.out.println("Enter effect: ");
                            short effectInput = getInput.nextShort();
                            getInput.nextLine(); // This line has to be added as buffer or else terminal will skip over next reading
                            short[] rgbList = hexToRGB(hexCode);
                            lights[row][col] = new Light(rgbList[0], rgbList[1], rgbList[2], effectInput);
                        } else {
                            lights[row][col] = new Light(); // each cell needs to be constructed
                        }  
                    }
                }
        }
        
        getInput.close();
    }

    // Many constructors for many different options
    public LightBoard(int numRows, int numCols) {
        this(numRows, numCols, false, "");
    }

    public LightBoard(int numRows, int numCols, boolean input) {
        this(numRows, numCols, true, "");
    }

    public LightBoard(int numRows, int numCols, String pattern) {
        this(numRows, numCols, true, pattern);
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
        LightBoard lightBoard = new LightBoard(5, 5, "frame");
        System.out.println(lightBoard);  // use toString() method
        System.out.println(lightBoard.toTerminal());
        System.out.println(lightBoard.toColorPalette());
    }
}