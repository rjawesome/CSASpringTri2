package com.nighthawk.spring_portfolio.mvc.frqs.lightboard1;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

public class Light1 {
    boolean on;
    boolean evaluates;
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
		colorMap.put("IndianRed".toLowerCase(), "#CD5C5C");
		colorMap.put("LightCoral".toLowerCase(), "#F08080");
		colorMap.put("Salmon".toLowerCase(), "#FA8072");
		colorMap.put("DarkSalmon".toLowerCase(), "#E9967A");
		colorMap.put("LightSalmon".toLowerCase(), "#FFA07A");
		colorMap.put("Crimson".toLowerCase(), "#DC143C");
		colorMap.put("Red".toLowerCase(), "#FF0000");
		colorMap.put("FireBrick".toLowerCase(), "#B22222");
		colorMap.put("DarkRed".toLowerCase(), "#8B0000");
		colorMap.put("Pink".toLowerCase(), "#FFC0CB");
		colorMap.put("LightPink".toLowerCase(), "#FFB6C1");
		colorMap.put("HotPink".toLowerCase(), "#FF69B4");
		colorMap.put("DeepPink".toLowerCase(), "#FF1493");
		colorMap.put("MediumVioletRed".toLowerCase(), "#C71585");
		colorMap.put("PaleVioletRed".toLowerCase(), "#DB7093");
		colorMap.put("LightSalmon".toLowerCase(), "#FFA07A");
		colorMap.put("Coral".toLowerCase(), "#FF7F50");
		colorMap.put("Tomato".toLowerCase(), "#FF6347");
		colorMap.put("OrangeRed".toLowerCase(), "#FF4500");
		colorMap.put("DarkOrange".toLowerCase(), "#FF8C00");
		colorMap.put("Orange".toLowerCase(), "#FFA500");
		colorMap.put("Gold".toLowerCase(), "#FFD700");
		colorMap.put("Yellow".toLowerCase(), "#FFFF00");
		colorMap.put("LightYellow".toLowerCase(), "#FFFFE0");
		colorMap.put("LemonChiffon".toLowerCase(), "#FFFACD");
		colorMap.put("LightGoldenrodYellow".toLowerCase(), "#FAFAD2");
		colorMap.put("PapayaWhip".toLowerCase(), "#FFEFD5");
		colorMap.put("Moccasin".toLowerCase(), "#FFE4B5");
		colorMap.put("PeachPuff".toLowerCase(), "#FFDAB9");
		colorMap.put("PaleGoldenrod".toLowerCase(), "#EEE8AA");
		colorMap.put("Khaki".toLowerCase(), "#F0E68C");
		colorMap.put("DarkKhaki".toLowerCase(), "#BDB76B");
		colorMap.put("Lavender".toLowerCase(), "#E6E6FA");
		colorMap.put("Thistle".toLowerCase(), "#D8BFD8");
		colorMap.put("Plum".toLowerCase(), "#DDA0DD");
		colorMap.put("Violet".toLowerCase(), "#EE82EE");
		colorMap.put("Orchid".toLowerCase(), "#DA70D6");
		colorMap.put("Fuchsia".toLowerCase(), "#FF00FF");
		colorMap.put("Magenta".toLowerCase(), "#FF00FF");
		colorMap.put("MediumOrchid".toLowerCase(), "#BA55D3");
		colorMap.put("MediumPurple".toLowerCase(), "#9370DB");
		colorMap.put("RebeccaPurple".toLowerCase(), "#663399");
		colorMap.put("BlueViolet".toLowerCase(), "#8A2BE2");
		colorMap.put("DarkViolet".toLowerCase(), "#9400D3");
		colorMap.put("DarkOrchid".toLowerCase(), "#9932CC");
		colorMap.put("DarkMagenta".toLowerCase(), "#8B008B");
		colorMap.put("Purple".toLowerCase(), "#800080");
		colorMap.put("Indigo".toLowerCase(), "#4B0082");
		colorMap.put("SlateBlue".toLowerCase(), "#6A5ACD");
		colorMap.put("DarkSlateBlue".toLowerCase(), "#483D8B");
		colorMap.put("MediumSlateBlue".toLowerCase(), "#7B68EE");
		colorMap.put("GreenYellow".toLowerCase(), "#ADFF2F");
		colorMap.put("Chartreuse".toLowerCase(), "#7FFF00");
		colorMap.put("LawnGreen".toLowerCase(), "#7CFC00");
		colorMap.put("Lime".toLowerCase(), "#00FF00");
		colorMap.put("LimeGreen".toLowerCase(), "#32CD32");
		colorMap.put("PaleGreen".toLowerCase(), "#98FB98");
		colorMap.put("LightGreen".toLowerCase(), "#90EE90");
		colorMap.put("MediumSpringGreen".toLowerCase(), "#00FA9A");
		colorMap.put("SpringGreen".toLowerCase(), "#00FF7F");
		colorMap.put("MediumSeaGreen".toLowerCase(), "#3CB371");
		colorMap.put("SeaGreen".toLowerCase(), "#2E8B57");
		colorMap.put("ForestGreen".toLowerCase(), "#228B22");
		colorMap.put("Green".toLowerCase(), "#008000");
		colorMap.put("DarkGreen".toLowerCase(), "#006400");
		colorMap.put("YellowGreen".toLowerCase(), "#9ACD32");
		colorMap.put("OliveDrab".toLowerCase(), "#6B8E23");
		colorMap.put("Olive".toLowerCase(), "#808000");
		colorMap.put("DarkOliveGreen".toLowerCase(), "#556B2F");
		colorMap.put("MediumAquamarine".toLowerCase(), "#66CDAA");
		colorMap.put("DarkSeaGreen".toLowerCase(), "#8FBC8B");
		colorMap.put("LightSeaGreen".toLowerCase(), "#20B2AA");
		colorMap.put("DarkCyan".toLowerCase(), "#008B8B");
		colorMap.put("Teal".toLowerCase(), "#008080");
		colorMap.put("Aqua".toLowerCase(), "#00FFFF");
		colorMap.put("Cyan".toLowerCase(), "#00FFFF");
		colorMap.put("LightCyan".toLowerCase(), "#E0FFFF");
		colorMap.put("PaleTurquoise".toLowerCase(), "#AFEEEE");
		colorMap.put("Aquamarine".toLowerCase(), "#7FFFD4");
		colorMap.put("Turquoise".toLowerCase(), "#40E0D0");
		colorMap.put("MediumTurquoise".toLowerCase(), "#48D1CC");
		colorMap.put("DarkTurquoise".toLowerCase(), "#00CED1");
		colorMap.put("CadetBlue".toLowerCase(), "#5F9EA0");
		colorMap.put("SteelBlue".toLowerCase(), "#4682B4");
		colorMap.put("LightSteelBlue".toLowerCase(), "#B0C4DE");
		colorMap.put("PowderBlue".toLowerCase(), "#B0E0E6");
		colorMap.put("LightBlue".toLowerCase(), "#ADD8E6");
		colorMap.put("SkyBlue".toLowerCase(), "#87CEEB");
		colorMap.put("LightSkyBlue".toLowerCase(), "#87CEFA");
		colorMap.put("DeepSkyBlue".toLowerCase(), "#00BFFF");
		colorMap.put("DodgerBlue".toLowerCase(), "#1E90FF");
		colorMap.put("CornflowerBlue".toLowerCase(), "#6495ED");
		colorMap.put("MediumSlateBlue".toLowerCase(), "#7B68EE");
		colorMap.put("RoyalBlue".toLowerCase(), "#4169E1");
		colorMap.put("Blue".toLowerCase(), "#0000FF");
		colorMap.put("MediumBlue".toLowerCase(), "#0000CD");
		colorMap.put("DarkBlue".toLowerCase(), "#00008B");
		colorMap.put("Navy".toLowerCase(), "#000080");
		colorMap.put("MidnightBlue".toLowerCase(), "#191970");
		colorMap.put("Cornsilk".toLowerCase(), "#FFF8DC");
		colorMap.put("BlanchedAlmond".toLowerCase(), "#FFEBCD");
		colorMap.put("Bisque".toLowerCase(), "#FFE4C4");
		colorMap.put("NavajoWhite".toLowerCase(), "#FFDEAD");
		colorMap.put("Wheat".toLowerCase(), "#F5DEB3");
		colorMap.put("BurlyWood".toLowerCase(), "#DEB887");
		colorMap.put("Tan".toLowerCase(), "#D2B48C");
		colorMap.put("RosyBrown".toLowerCase(), "#BC8F8F");
		colorMap.put("SandyBrown".toLowerCase(), "#F4A460");
		colorMap.put("Goldenrod".toLowerCase(), "#DAA520");
		colorMap.put("DarkGoldenrod".toLowerCase(), "#B8860B");
		colorMap.put("Peru".toLowerCase(), "#CD853F");
		colorMap.put("Chocolate".toLowerCase(), "#D2691E");
		colorMap.put("SaddleBrown".toLowerCase(), "#8B4513");
		colorMap.put("Sienna".toLowerCase(), "#A0522D");
		colorMap.put("Brown".toLowerCase(), "#A52A2A");
		colorMap.put("Maroon".toLowerCase(), "#800000");
		colorMap.put("White".toLowerCase(), "#FFFFFF");
		colorMap.put("Snow".toLowerCase(), "#FFFAFA");
		colorMap.put("HoneyDew".toLowerCase(), "#F0FFF0");
		colorMap.put("MintCream".toLowerCase(), "#F5FFFA");
		colorMap.put("Azure".toLowerCase(), "#F0FFFF");
		colorMap.put("AliceBlue".toLowerCase(), "#F0F8FF");
		colorMap.put("GhostWhite".toLowerCase(), "#F8F8FF");
		colorMap.put("WhiteSmoke".toLowerCase(), "#F5F5F5");
		colorMap.put("SeaShell".toLowerCase(), "#FFF5EE");
		colorMap.put("Beige".toLowerCase(), "#F5F5DC");
		colorMap.put("OldLace".toLowerCase(), "#FDF5E6");
		colorMap.put("FloralWhite".toLowerCase(), "#FFFAF0");
		colorMap.put("Ivory".toLowerCase(), "#FFFFF0");
		colorMap.put("AntiqueWhite".toLowerCase(), "#FAEBD7");
		colorMap.put("Linen".toLowerCase(), "#FAF0E6");
		colorMap.put("LavenderBlush".toLowerCase(), "#FFF0F5");
		colorMap.put("MistyRose".toLowerCase(), "#FFE4E1");
		colorMap.put("Gainsboro".toLowerCase(), "#DCDCDC");
		colorMap.put("LightGray".toLowerCase(), "#D3D3D3");
		colorMap.put("Silver".toLowerCase(), "#C0C0C0");
		colorMap.put("DarkGray".toLowerCase(), "#A9A9A9");
		colorMap.put("Gray".toLowerCase(), "#808080");
		colorMap.put("DimGray".toLowerCase(), "#696969");
		colorMap.put("LightSlateGray".toLowerCase(), "#778899");
		colorMap.put("SlateGray".toLowerCase(), "#708090");
		colorMap.put("DarkSlateGray".toLowerCase(), "#2F4F4F");
		colorMap.put("Black".toLowerCase(), "#000000");
	}
	

    /* Assign random colors and effects */
    public Light1() {
        int maxColor = 255;
        int maxEffect = 9;
        this.red = (short) (Math.random()*(maxColor+1));
        this.green = (short) (Math.random()*(maxColor+1));
        this.blue = (short) (Math.random()*(maxColor+1));
        this.effect = (short) (Math.random()*(maxEffect+1));
        this.on = Math.random() > 0.5;
    }
	
	/* Use color name input */
	public Light1(String colorName) {
    int maxEffect = 9;
		String hex = colorMap.get(colorName.toLowerCase().replaceAll(" ", ""));
		Color colorObj = Color.decode(hex);
		this.red = (short) colorObj.getRed();
		this.green = (short) colorObj.getGreen();
		this.blue = (short) colorObj.getBlue();
		this.effect = (short) (Math.random()*(maxEffect+1));
    this.on = Math.random() > 0.5;
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
            "\"effect\": " + "\"" + EFFECT.get(effect) + "\"" + "," +
            "\"on\": " + on + "," +
            "\"evaluates\": " + evaluates + 
            "}" );
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setEvaluates(boolean evaluates) {
      this.evaluates = evaluates;
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
        Light1 light = new Light1();
        System.out.println(light);  // use toString() method
    }
    

}
