package game.display;

import java.awt.*;

/**
 *
 */
public class Appearance {

  private final char character;
  private final Color color;
  private final Color bgColor;

  public Appearance(char character, Color color, Color bgColor) {
    this.character = character;
    this.color = color;
    this.bgColor = bgColor;
  }

  public Appearance(char character, Color color) {
    this(character,color,null);
  }

  public char getCharacter() {
    return character;
  }

  public Color getColor() {
    return color;
  }

  public Color getBGColor() {
    return bgColor;
  }

}
