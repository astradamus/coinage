package game.io.display;

import game.io.input.GameInput;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

/**
 *
 */
class SidePanel extends JPanel {

  public static final int SP_SQUARE_SIZE = 30;
  public static final int SP_SQUARES_WIDE = 13;
  public static final Font PROMPT_FONT = new Font("Serif", Font.BOLD, SP_SQUARE_SIZE);
  public static final Font MEDIUM_FONT = new Font("Serif", Font.PLAIN, SP_SQUARE_SIZE * 8 / 9);
  private static final int SP_TEXT_SIZE = SP_SQUARE_SIZE * 3 / 7;
  public static final Font CONTROLS_FONT = new Font("Monospaced", Font.PLAIN, SP_TEXT_SIZE);
  private static final int SP_BORDER_SQUARES_X = 1;
  private static final int SP_BORDER_SQUARES_Y = 1;
  private static final int SP_LEFT_EDGE = SP_BORDER_SQUARES_X * SP_SQUARE_SIZE;
  private static final int SP_TOP_EDGE = SP_BORDER_SQUARES_Y * SP_SQUARE_SIZE;
  private static final Font MAP_FONT = new Font("SansSerif", Font.BOLD, SP_SQUARE_SIZE);

  {
    setBackground(Color.BLACK);
    setFont(MAP_FONT);
  }


  @Override
  public void paint(Graphics g) {
    super.paint(g);

    int pixelsDown = SP_TOP_EDGE;

    List<DisplayElement> displayElements = GameInput.getGameMode().getDisplayElements();

    for (DisplayElement displayElement : displayElements) {
      if (displayElement == null) {
        continue;
      }
      displayElement.drawTo(g, SP_LEFT_EDGE, pixelsDown, getWidth());
      pixelsDown += displayElement.getHeight();
    }
  }
}