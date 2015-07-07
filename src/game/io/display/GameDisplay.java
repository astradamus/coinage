package game.io.display;


import game.Game;
import game.physical.Appearance;
import world.World;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.security.InvalidParameterException;
import java.util.List;

/**
 *
 */
public class GameDisplay {

  public static final Appearance CURSOR = new Appearance(' ', Color.WHITE, Color.WHITE);

  public static final int SQUARE_SIZE = 20;

  private static Game runningGame;

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

  public static Game getRunningGame() {
    return runningGame;
  }

  public static void recalculateSize() {
    World world = runningGame.getWorld();
    int areaPanelWidth = (world.getAreaSizeInSquares().getWidth() +1)*SQUARE_SIZE;
    int areaPanelHeight = (world.getAreaSizeInSquares().getHeight() +1)*SQUARE_SIZE;

    int sidePanelWidth = SidePanel.SP_SQUARES_WIDE * SidePanel.SP_SQUARE_SIZE;

    PANEL_AREA.setMaximumSize(new Dimension(areaPanelWidth,areaPanelHeight));
    PANEL_SIDE.setMaximumSize(new Dimension(sidePanelWidth,areaPanelHeight));

    WINDOW.setSize(areaPanelWidth + sidePanelWidth, areaPanelHeight + WINDOW.getInsets().top);
  }

  public static void onUpdate() {
    PANEL_AREA.repaint();
    PANEL_SIDE.repaint();
  }

  public static void loadRunningGame(Game activeGame) {
    if (GameDisplay.runningGame != null) {
      throw new IllegalStateException("Already running a game, must first call unloadRunningGame().");
    }
    GameDisplay.runningGame = activeGame;
  }

  /**
   * @param runningGame Must be supplied to ensure this method is only called from high places.
   * @throws InvalidParameterException If the supplied game is null or does not match the currently
   *                    running game.
   */
  public static void unloadRunningGame(Game runningGame) {
    if (GameDisplay.runningGame != runningGame) {
      throw new InvalidParameterException("Game parameter does not match currently running game.");
    }
    GameDisplay.runningGame = null;
  }

  public static void addKeyListeners(List<KeyListener> keyListeners) {
    keyListeners.forEach(WINDOW::addKeyListener);
  }

}
