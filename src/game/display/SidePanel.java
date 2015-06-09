package game.display;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 */
public class SidePanel extends JPanel {


  {
    setBackground(Color.BLACK);
    setFont(MAP_FONT);
  }


  public static final int SP_SQUARE_SIZE = 30;
  public static final int SP_TEXT_SIZE = SP_SQUARE_SIZE * 3 / 7;

  public static final int SP_SQUARES_WIDE = 13;
  public static final int SP_BORDER_SQUARES_X = 1;
  public static final int SP_BORDER_SQUARES_Y = 1;
  public static final int SP_LEFT_EDGE = SP_BORDER_SQUARES_X * SP_SQUARE_SIZE;
  public static final int SP_TOP_EDGE = SP_BORDER_SQUARES_Y * SP_SQUARE_SIZE;

  public static final Font MAP_FONT = new Font("SansSerif", Font.BOLD, SP_SQUARE_SIZE);
  public static final Font PROMPT_FONT = new Font("Serif", Font.BOLD, SP_SQUARE_SIZE);
  public static final Font MEDIUM_FONT = new Font("Serif", Font.PLAIN, SP_SQUARE_SIZE*8/9);
  public static final Font CONTROLS_FONT = new Font("Monospaced", Font.PLAIN, SP_TEXT_SIZE);



  @Override
  public void paint(Graphics g) {
    super.paint(g);

    int pixelsDown = SP_TOP_EDGE;

    List<DisplayElement> displayElements =
        Game.getActiveInputSwitch().getGameMode().getDisplayElements();

    for (DisplayElement displayElement : displayElements) {
      if (displayElement == null) {
        continue;
      }
      displayElement.drawTo(g, SP_LEFT_EDGE, pixelsDown, getWidth());
      pixelsDown += displayElement.getHeight();
    }
  }

}