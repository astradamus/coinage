package game.display;

import controller.PlayerController;
import game.Game;
import game.Physical;
import world.Area;
import world.World;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class AreaPanel extends JPanel {


  private static final int TILE_SIZE = GameDisplay.TILE_SIZE;

  public AreaPanel() {
    setBackground(Color.BLACK);
    setFont(new Font("Monospace", Font.BOLD, TILE_SIZE));
  }


  @Override
  public void paint(Graphics g) {
    super.paint(g);

    PlayerController pC = Game.getActive().CONTROLLERS.getPlayerController();
    Point playerCoordinate = pC.getGlobalCoordinate();

    World world = Game.getActive().WORLD;
    Area area = world.getAreaByGlobalCoordinate(playerCoordinate.x,playerCoordinate.y);

    for (int y = 0; y < world.getAreaSizeInSquares().getHeight(); y++) {
      for (int x = 0; x < world.getAreaSizeInSquares().getWidth(); x++) {

        Physical visible = area.getPhysicalsComponent().getPriorityPhysical(x, y);

        int placeX = (x) * TILE_SIZE;
        int placeY = (y + 1) * TILE_SIZE;

        Color bgColor = visible.getBGColor();
        Color color = visible.getColor();
        if (bgColor != null) {
          g.setColor(bgColor);
          g.fillRect((placeX - TILE_SIZE / 6), (int) (placeY - TILE_SIZE * 0.85), TILE_SIZE,
              TILE_SIZE);
        }

        g.setColor(color);
        g.drawChars(new char[]{visible.getAppearance()}, 0, 1, placeX, placeY);
      }
    }
  }
}
