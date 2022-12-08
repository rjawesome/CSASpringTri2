package com.nighthawk.spring_portfolio.mvc.lightboard1;
import java.util.HashMap;
import java.util.Map;

public class Light1 {
    boolean on;
    short red;
    short green;
    short blue;
    short effect;

    /*  ANSI effects
        n	Name	Note
        0	Reset or normal	All attributes off
        1	Bold or increased intensity	As with faint, the color change is a PC (SCO / CGA) invention.[38][better source needed]
        2	Faint, decreased intensity, or dim	May be implemented as a light font weight like bold.[39]
        3	Italic	Not widely supported. Sometimes treated as inverse or blink.[38]
        4	Underline	Style extensions exist for Kitty, VTE, mintty and iTerm2.[40][41]
        5	Slow blink	Sets blinking to less than 150 times per minute
        6	Rapid blink	MS-DOS ANSI.SYS, 150+ per minute; not widely supported
        7	Reverse video or invert	Swap foreground and background colors; inconsistent emulation[42]
        8	Conceal or hide	Not widely supported.
        9	Crossed-out, or strike	Characters legible but marked as if for deletion. Not supported in Terminal.app
     */
    private final Map<Short, String> EFFECT = new HashMap<>();
    {
        // Map<"separator", not_used>
        EFFECT.put((short) 0, "Normal");
        EFFECT.put((short) 1, "Bold");
        EFFECT.put((short) 2, "Faint");
        EFFECT.put((short) 3, "Italic");
        EFFECT.put((short) 4, "Underline");
        EFFECT.put((short) 5, "Slow Blink");
        EFFECT.put((short) 6, "Fast Blink");
        EFFECT.put((short) 7, "Reverse");
        EFFECT.put((short) 8, "Conceal");
        EFFECT.put((short) 9, "Crossed_out");
    }
	
	private final Map<String, String> colorMap = new HashMap<>();
	{
		colorMap.put("IndianRed", "#CD5C5C");
		colorMap.put("LightCoral", "#F08080");
		colorMap.put("Salmon", "#FA8072");
		colorMap.put("DarkSalmon", "#E9967A");
		colorMap.put("LightSalmon", "#FFA07A");
		colorMap.put("Crimson", "#DC143C");
		colorMap.put("Red", "#FF0000");
		colorMap.put("FireBrick", "#B22222");
		colorMap.put("DarkRed", "#8B0000");
		colorMap.put("Pink", "#FFC0CB");
		colorMap.put("LightPink", "#FFB6C1");
		colorMap.put("HotPink", "#FF69B4");
		colorMap.put("DeepPink", "#FF1493");
		colorMap.put("MediumVioletRed", "#C71585");
		colorMap.put("PaleVioletRed", "#DB7093");
		colorMap.put("LightSalmon", "#FFA07A");
		colorMap.put("Coral", "#FF7F50");
		colorMap.put("Tomato", "#FF6347");
		colorMap.put("OrangeRed", "#FF4500");
		colorMap.put("DarkOrange", "#FF8C00");
		colorMap.put("Orange", "#FFA500");
		colorMap.put("Gold", "#FFD700");
		colorMap.put("Yellow", "#FFFF00");
		colorMap.put("LightYellow", "#FFFFE0");
		colorMap.put("LemonChiffon", "#FFFACD");
		colorMap.put("LightGoldenrodYellow", "#FAFAD2");
		colorMap.put("PapayaWhip", "#FFEFD5");
		colorMap.put("Moccasin", "#FFE4B5");
		colorMap.put("PeachPuff", "#FFDAB9");
		colorMap.put("PaleGoldenrod", "#EEE8AA");
		colorMap.put("Khaki", "#F0E68C");
		colorMap.put("DarkKhaki", "#BDB76B");
		colorMap.put("Lavender", "#E6E6FA");
		colorMap.put("Thistle", "#D8BFD8");
		colorMap.put("Plum", "#DDA0DD");
		colorMap.put("Violet", "#EE82EE");
		colorMap.put("Orchid", "#DA70D6");
		colorMap.put("Fuchsia", "#FF00FF");
		colorMap.put("Magenta", "#FF00FF");
		colorMap.put("MediumOrchid", "#BA55D3");
		colorMap.put("MediumPurple", "#9370DB");
		colorMap.put("RebeccaPurple", "#663399");
		colorMap.put("BlueViolet", "#8A2BE2");
		colorMap.put("DarkViolet", "#9400D3");
		colorMap.put("DarkOrchid", "#9932CC");
		colorMap.put("DarkMagenta", "#8B008B");
		colorMap.put("Purple", "#800080");
		colorMap.put("Indigo", "#4B0082");
		colorMap.put("SlateBlue", "#6A5ACD");
		colorMap.put("DarkSlateBlue", "#483D8B");
		colorMap.put("MediumSlateBlue", "#7B68EE");
		colorMap.put("GreenYellow", "#ADFF2F");
		colorMap.put("Chartreuse", "#7FFF00");
		colorMap.put("LawnGreen", "#7CFC00");
		colorMap.put("Lime", "#00FF00");
		colorMap.put("LimeGreen", "#32CD32");
		colorMap.put("PaleGreen", "#98FB98");
		colorMap.put("LightGreen", "#90EE90");
		colorMap.put("MediumSpringGreen", "#00FA9A");
		colorMap.put("SpringGreen", "#00FF7F");
		colorMap.put("MediumSeaGreen", "#3CB371");
		colorMap.put("SeaGreen", "#2E8B57");
		colorMap.put("ForestGreen", "#228B22");
		colorMap.put("Green", "#008000");
		colorMap.put("DarkGreen", "#006400");
		colorMap.put("YellowGreen", "#9ACD32");
		colorMap.put("OliveDrab", "#6B8E23");
		colorMap.put("Olive", "#808000");
		colorMap.put("DarkOliveGreen", "#556B2F");
		colorMap.put("MediumAquamarine", "#66CDAA");
		colorMap.put("DarkSeaGreen", "#8FBC8B");
		colorMap.put("LightSeaGreen", "#20B2AA");
		colorMap.put("DarkCyan", "#008B8B");
		colorMap.put("Teal", "#008080");
		colorMap.put("Aqua", "#00FFFF");
		colorMap.put("Cyan", "#00FFFF");
		colorMap.put("LightCyan", "#E0FFFF");
		colorMap.put("PaleTurquoise", "#AFEEEE");
		colorMap.put("Aquamarine", "#7FFFD4");
		colorMap.put("Turquoise", "#40E0D0");
		colorMap.put("MediumTurquoise", "#48D1CC");
		colorMap.put("DarkTurquoise", "#00CED1");
		colorMap.put("CadetBlue", "#5F9EA0");
		colorMap.put("SteelBlue", "#4682B4");
		colorMap.put("LightSteelBlue", "#B0C4DE");
		colorMap.put("PowderBlue", "#B0E0E6");
		colorMap.put("LightBlue", "#ADD8E6");
		colorMap.put("SkyBlue", "#87CEEB");
		colorMap.put("LightSkyBlue", "#87CEFA");
		colorMap.put("DeepSkyBlue", "#00BFFF");
		colorMap.put("DodgerBlue", "#1E90FF");
		colorMap.put("CornflowerBlue", "#6495ED");
		colorMap.put("MediumSlateBlue", "#7B68EE");
		colorMap.put("RoyalBlue", "#4169E1");
		colorMap.put("Blue", "#0000FF");
		colorMap.put("MediumBlue", "#0000CD");
		colorMap.put("DarkBlue", "#00008B");
		colorMap.put("Navy", "#000080");
		colorMap.put("MidnightBlue", "#191970");
		colorMap.put("Cornsilk", "#FFF8DC");
		colorMap.put("BlanchedAlmond", "#FFEBCD");
		colorMap.put("Bisque", "#FFE4C4");
		colorMap.put("NavajoWhite", "#FFDEAD");
		colorMap.put("Wheat", "#F5DEB3");
		colorMap.put("BurlyWood", "#DEB887");
		colorMap.put("Tan", "#D2B48C");
		colorMap.put("RosyBrown", "#BC8F8F");
		colorMap.put("SandyBrown", "#F4A460");
		colorMap.put("Goldenrod", "#DAA520");
		colorMap.put("DarkGoldenrod", "#B8860B");
		colorMap.put("Peru", "#CD853F");
		colorMap.put("Chocolate", "#D2691E");
		colorMap.put("SaddleBrown", "#8B4513");
		colorMap.put("Sienna", "#A0522D");
		colorMap.put("Brown", "#A52A2A");
		colorMap.put("Maroon", "#800000");
		colorMap.put("White", "#FFFFFF");
		colorMap.put("Snow", "#FFFAFA");
		colorMap.put("HoneyDew", "#F0FFF0");
		colorMap.put("MintCream", "#F5FFFA");
		colorMap.put("Azure", "#F0FFFF");
		colorMap.put("AliceBlue", "#F0F8FF");
		colorMap.put("GhostWhite", "#F8F8FF");
		colorMap.put("WhiteSmoke", "#F5F5F5");
		colorMap.put("SeaShell", "#FFF5EE");
		colorMap.put("Beige", "#F5F5DC");
		colorMap.put("OldLace", "#FDF5E6");
		colorMap.put("FloralWhite", "#FFFAF0");
		colorMap.put("Ivory", "#FFFFF0");
		colorMap.put("AntiqueWhite", "#FAEBD7");
		colorMap.put("Linen", "#FAF0E6");
		colorMap.put("LavenderBlush", "#FFF0F5");
		colorMap.put("MistyRose", "#FFE4E1");
		colorMap.put("Gainsboro", "#DCDCDC");
		colorMap.put("LightGray", "#D3D3D3");
		colorMap.put("Silver", "#C0C0C0");
		colorMap.put("DarkGray", "#A9A9A9");
		colorMap.put("Gray", "#808080");
		colorMap.put("DimGray", "#696969");
		colorMap.put("LightSlateGray", "#778899");
		colorMap.put("SlateGray", "#708090");
		colorMap.put("DarkSlateGray", "#2F4F4F");
		colorMap.put("Black", "#000000");
	}
	

    /* Assign random colors and effects */
    public Light() {
        int maxColor = 255;
        int effect = 9;
        this.red = (short) (Math.random()*(maxColor+1));
        this.green = (short) (Math.random()*(maxColor+1));
        this.blue = (short) (Math.random()*(maxColor+1));
        this.effect = (short) (Math.random()*(effect+1));
    }

    public String getEffectTitle() {
        return EFFECT.get(this.effect);
    }

    public String getRGB() {
        return ( "#" +
         String.format("%02X", this.red) +
         String.format("%02X", this.green) + 
         String.format("%02X", this.blue) 
         );
    }

    /* toString output as key/values */
    public String toString() {
        return( "{" + 
            "\"red\": " + red + "," +
            "\"green\": " +  green + "," + 
            "\"blue\": " + blue + "," +
            "\"effect\": " + "\"" + EFFECT.get(effect) + "\"" +
            "}" );
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public short getRed() {
        return red;
    }

    public short getGreen() {
        return green;
    }

    public short getBlue() {
        return blue;
    }

    public short getEffect() {
        return effect;
    }

    static public void main(String[] args) {
        // create and display LightBoard
        Light light = new Light();
        System.out.println(light);  // use toString() method
    }
    

}
Light.main(null);
{"red": 117,"green": 65,"blue": 98,"effect": "Fast Blink"}
public class LightBoard {
    private Light[][] lights;

    /* Initialize LightBoard and Lights */
    public LightBoard(int numRows, int numCols) {
        this.lights = new Light[numRows][numCols];
        // 2D array nested loops, used for initialization
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                lights[row][col] = new Light();  // each cell needs to be constructed
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
    
    static public void main(String[] args) {
        // create and display LightBoard
        LightBoard lightBoard = new LightBoard(5, 5);
        System.out.println(lightBoard);  // use toString() method
        System.out.println(lightBoard.toTerminal());
        System.out.println(lightBoard.toColorPalette());
    }
}
