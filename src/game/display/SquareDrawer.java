package game.display;

import game.physical.Appearance;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 *
 */
public class SquareDrawer {

  public static void drawSquare(Graphics g, Appearance appearance, int squareSize,
                                int drawX, int drawY) {

    drawSquare(g, appearance.getMapSymbol(), appearance.getColor(), appearance.getBGColor(),
        squareSize, drawX, drawY);

  }

  public static void drawSquare(Graphics g, char mapSymbol, Color color, Color bgColor,
                                int squareSize, int drawX, int drawY) {

    if (bgColor != null) {
      g.setColor(bgColor);
      g.fillRect(drawX, drawY, squareSize, squareSize);
    }

    String chars = String.valueOf(mapSymbol);
    g.setColor(color);
    FontMetrics fM = g.getFontMetrics();
    int centerX = drawX + (squareSize - fM.stringWidth(chars)) / 2;
    int centerY = drawY + fM.getAscent() + (squareSize - fM.getHeight())/2 - 1;
    g.drawString(chars, centerX, centerY);
  }

  public static void drawOval(Graphics g, Appearance appearance, int squareSize, int drawX, int drawY) {

    char character = appearance.getMapSymbol();
    Color color = appearance.getColor();
    Color bgColor = appearance.getBGColor();

    if (bgColor != null) {
      g.setColor(bgColor);
      g.drawOval(drawX - 1, drawY - 1, squareSize, squareSize);
    }

    String chars = String.valueOf(character);
    g.setColor(color);
    FontMetrics fM = g.getFontMetrics();
    int centerX = drawX + (squareSize - fM.stringWidth(chars)) / 2;
    int centerY = drawY + fM.getAscent() + (squareSize - fM.getHeight())/2 - 1;
    g.drawString(chars, centerX, centerY);

  }

  public static void drawString(Graphics g, String string, Color color, int drawX, int drawY) {

      g.setColor(color);
      g.drawString(string, drawX, drawY);

  }

}
