package com.nighthawk.spring_portfolio.mvc.frqs.lightboard1;

public class LightBoard1 {
    private Light1[][] lights;

    /* Initialize LightBoard and Lights */
    public LightBoard1(int numRows, int numCols) {
        this.lights = new Light1[numRows][numCols];
        // 2D array nested loops, used for initialization
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                lights[row][col] = new Light1();  // each cell needs to be constructed
            }
        }

        this.evaluateLights();
    }

    /* Initialize LightBoard and Lights w/ single color */
    public LightBoard1(int numRows, int numCols, String color) {
      this.lights = new Light1[numRows][numCols];
      // 2D array nested loops, used for initialization
      for (int row = 0; row < numRows; row++) {
          for (int col = 0; col < numCols; col++) {
              lights[row][col] = new Light1(color);  // each cell needs to be constructed
          }
      }

      this.evaluateLights();
  }

  public void evaluateLights () {
    LightboardFRQ frq = new LightboardFRQ(this);
    for (int row = 0; row < lights.length; row++) {
      for (int col = 0; col < lights[row].length; col++){
        lights[row][col].setEvaluates(frq.evaluateLight(row, col));
      }
    }
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

    public Light1[][] getLights() {
        return this.lights;
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
        final int ROWS = 7;
        final int COLS = 8;

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
                        (lights[row][col].on ? ("\033[38;2;" + 
                        lights[row][col].getRed() + ";" +
                        lights[row][col].getGreen() + ";" +
                        lights[row][col].getBlue() + ";" +
                        (lights[row][col].evaluates ? "7m" : "9m")) : (lights[row][col].evaluates ? "" : "\033[38;2;;;;9m")) +

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
    
    static public void main(String[] args) {
        // create and display LightBoard
        LightBoard1 lightBoard = new LightBoard1(6, 6, "Light Green");
        System.out.println(lightBoard);  // use toString() method
        System.out.println(lightBoard.toTerminal());
        System.out.println(lightBoard.toColorPalette());

        LightBoard1 lightBoard2 = new LightBoard1(6, 6);
        System.out.println(lightBoard2);  // use toString() method
        System.out.println(lightBoard2.toTerminal());
        System.out.println(lightBoard2.toColorPalette());
    }
    
}
