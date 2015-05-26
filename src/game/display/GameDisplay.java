package game.display;


import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 *
 */
public class GameDisplay {

  public static final int TILE_SIZE = 15;


  private static AreaPanel PANEL_AREA = new AreaPanel();
  private static SidePanel PANEL_SIDE = new SidePanel();


  private static JFrame WINDOW = new JFrame("Coinage") {
    {
      JPanel container = new JPanel();
      container.setBackground(Color.BLACK);
      container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

      container.add(PANEL_AREA);
      container.add(PANEL_SIDE);

      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      add(container);
      setVisible(true);
    }
  };

  public static void recalculateSize() {
    int areaPanelWidth = (Game.getActive().WORLD.areaWidth + 1) * TILE_SIZE;
    int areaPanelHeight = (Game.getActive().WORLD.areaHeight + 1) * TILE_SIZE;

    int sidePanelWidth = SidePanel.TILES_WIDE * SidePanel.TILE_SIZE;

    PANEL_AREA.setMaximumSize(new Dimension(areaPanelWidth,areaPanelHeight));
    PANEL_SIDE.setMaximumSize(new Dimension(sidePanelWidth,areaPanelHeight));

    WINDOW.setSize(areaPanelWidth + sidePanelWidth, areaPanelHeight + WINDOW.getInsets().top);
  }

  public static void onUpdate() {
    PANEL_AREA.repaint();
    PANEL_SIDE.repaint();
  }

  public static void addKeyListener(KeyListener keyListener) {
    WINDOW.addKeyListener(keyListener);
  }
}
