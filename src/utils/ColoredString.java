package utils;

import java.awt.Color;

/**
 *
 */
public class ColoredString {

    private final String string;
    private final Color color;

    public ColoredString(Color color, String string) {
        this.string = string;
        this.color = color;
    }

    public String getString() {
        return string;
    }

    public Color getColor() {
        return color;
    }

}
