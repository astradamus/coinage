package game.display;

import game.Game;
import world.Coordinate;
import world.World;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class AreaPanel extends JPanel {


  private static final int SQUARE_SIZE = GameDisplay.SQUARE_SIZE;

  public AreaPanel() {
    setBackground(Color.BLACK);
    setFont(new Font("SansSerif", Font.BOLD, SQUARE_SIZE));
  }


  @Override
  public void paint(Graphics g) {
    super.paint(g);

    Coordinate playerAt = Game.getActivePlayer().getCoordinate();

    World world = Game.getActiveWorld();

    for (int y = 0; y < world.getAreaSizeInSquares().getHeight(); y++) {
      for (int x = 0; x < world.getAreaSizeInSquares().getWidth(); x++) {

        Coordinate thisCoordinate = world.offsetCoordinateBySquares(playerAt,
            x-playerAt.localX, y-playerAt.localY);

        Appearance visible = thisCoordinate.getSquare().peek().getAppearance();

        int placeX = (x) * SQUARE_SIZE;
        int placeY = (y + 1) * SQUARE_SIZE;

        SquareDrawer.drawSquare(g, visible, SQUARE_SIZE, placeX, placeY);

        Coordinate cursorTarget = Game.getActiveCursorTarget();
        if (cursorTarget != null && cursorTarget.localX == x && cursorTarget.localY == y) {
          SquareDrawer.drawOval(g, GameDisplay.CURSOR, SQUARE_SIZE, placeX, placeY);
        }

      }
    }
  }
}
