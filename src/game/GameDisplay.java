package game;

import controller.PlayerController;
import world.Area;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 *
 */
public class GameDisplay {

  private static final int TILE_SIZE = 15;
  private static JPanel DISPLAY = new JPanel() {

    {
      setBackground(Color.BLACK);
      setFont(new Font("Droid Sans Mono", Font.BOLD, TILE_SIZE));
    }

    @Override
    public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2d = (Graphics2D) g;
      PlayerController pC = Game.getActive().CONTROLLERS.getPlayerController();
      Area area = Game.getActive().WORLD.getAreaFromGlobalCoordinate(pC.getX(),pC.getY());
      for (int y = 0; y < area.getHeight(); y++) {
        for (int x = 0; x < area.getWidth(); x++) {

          Physical visible = area.getPriorityPhysical(x, y);

          int placeX = (x) * TILE_SIZE;
          int placeY = (y + 1) * TILE_SIZE;

          Color bgColor = visible.getBGColor();
          Color color = visible.getColor();
          if (bgColor != null) {
            g2d.setColor(bgColor);
            g2d.fillRect((placeX - TILE_SIZE / 6), (int) (placeY - TILE_SIZE * 0.85), TILE_SIZE,
                TILE_SIZE);
          }

          g2d.setColor(color);
          g2d.drawChars(new char[]{visible.getAppearance()}, 0, 1, placeX, placeY);
        }
      }
    }

  };

  private static JFrame WINDOW = new JFrame("Coinage") {
    {
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      add(DISPLAY);
      setVisible(true);
    }
  };

  static void recalculateSize() {
    WINDOW.setSize((Game.getActive().WORLD.areaWidth + 1) * TILE_SIZE,
        (Game.getActive().WORLD.areaHeight + 1) * TILE_SIZE + WINDOW.getInsets().top);
  }

  static void onUpdate() {
    DISPLAY.repaint();
  }

  public static void addKeyListener(KeyListener keyListener) {
    WINDOW.addKeyListener(keyListener);
  }
}
