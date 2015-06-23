package game.physical;

import java.awt.Color;

/**
 *
 */
public class Appearance {


  private final char mapSymbol;
  private final Color color;
  private final Color bgColor;
  private final int visualPriority;

  public Appearance(char mapSymbol, Color color, Color bgColor, int visualPriority) {
    this.mapSymbol = mapSymbol;
    this.color = color;
    this.bgColor = bgColor;
    this.visualPriority = visualPriority;
  }

  public Appearance(char mapSymbol, Color color, Color bgColor) {
    this(mapSymbol, color, bgColor, 0);
  }

  public Appearance(char mapSymbol, Color color, int visualPriority) {
    this(mapSymbol, color, null, visualPriority);
  }

  public Appearance(char mapSymbol, Color color) {
    this(mapSymbol, color, null, 0);
  }


  public char getMapSymbol() {
    return mapSymbol;
  }

  public Color getColor() {
    return color;
  }

  public Color getBGColor() {
    return bgColor;
  }

  public int getVisualPriority() {
    return visualPriority;
  }

}