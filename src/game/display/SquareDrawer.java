package game.display;

import game.Physical;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;


/**
 *
 */
public class SquareDrawer {

  public static void drawSquare(Graphics g, Appearance appearance,
                                int squareSize, int drawX, int drawY) {

    char character = appearance.getCharacter();
    Color color = appearance.getColor();
    Color bgColor = appearance.getBGColor();

    if (bgColor != null) {
      g.setColor(bgColor);
      g.fillRect(drawX, calcBackgroundY(drawY, squareSize), squareSize,squareSize);
    }

    g.setColor(color);
    g.drawChars(new char[]{character}, 0, 1, calcForegroundX(drawX,squareSize), drawY);

  }

  public static void drawOval(Graphics g, Appearance appearance,
                              int squareSize, int drawX, int drawY) {

    char character = appearance.getCharacter();
    Color color = appearance.getColor();
    Color bgColor = appearance.getBGColor();

    if (bgColor != null) {
      g.setColor(bgColor);
      g.drawOval(drawX-1, calcBackgroundY(drawY, squareSize)-1, squareSize, squareSize);
    }

    g.setColor(color);
    g.drawChars(new char[]{character}, 0, 1, calcForegroundX(drawX,squareSize), drawY);

  }

  private static int calcForegroundX(int drawX, int squareSize) {
    return (int) (drawX+squareSize/6.5);
  }

  private static int calcBackgroundY(int drawY, int squareSize) {
    return (int) (drawY-squareSize*0.85);
  }

  public static void drawPhysicalsList(Graphics g, List<Physical> physicals, int squareSize,
                                       int drawX, int drawY) {

    for (int i = 0; i < physicals.size(); i++) {

      Physical physical = physicals.get(i);
      drawString(g, physical.getName(), physical.getAppearance().getColor(),
          drawX, drawY + i *squareSize);

    }

  }

  public static void drawString(Graphics g, String string, Color color, int drawX, int drawY) {

      g.setColor(color);
      g.drawString(string, drawX, drawY);

  }

  public static void drawStringList(Graphics g, List<String> strings, Color color, int squareSize,
                                    int drawX, int drawY) {

    for (int i = 0; i < strings.size(); i++) {

      String string = strings.get(i);

      drawString(g, string, color, drawX, drawY + i * squareSize);

    }

  }

}
