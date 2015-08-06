package game.io.better_ui;

import game.Game;
import game.io.better_ui.widget_ui.MouseControl;
import game.physical.Physical;
import utils.ImmutableDimension;
import world.AreaCoordinate;
import world.Coordinate;
import world.World;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 */
public class GamePanel extends JPanel {

  private final Game game;
  private final ImmutableDimension areaSize;

  private final int tileSize;
  private final Font gamePanelFont;

  private MouseControl mouseControl;


  public GamePanel(Game game, int tileSize) {

    this.game = game;
    areaSize = game.getWorld().getAreaSizeInSquares();

    this.tileSize = tileSize;
    gamePanelFont = new Font("SansSerif", Font.BOLD, tileSize);

    EventLog.initialize(game, tileSize);

    setBackground(Color.black);
    setPreferredSize(
        new java.awt.Dimension(tileSize * areaSize.getWidth(), tileSize * areaSize.getHeight()));

    mouseControl = new MouseControl(game, this);
    addMouseListener(mouseControl);
    addMouseMotionListener(mouseControl);

    new Timer(20, e -> repaint()).start();
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setFont(gamePanelFont);

    final World world = game.getWorld();

    Coordinate playerAt = game.getActivePlayerActor().getCoordinate();
    AreaCoordinate playerAtAC = world.convertToAreaCoordinate(playerAt);
    final Coordinate playerAreaOrigin = playerAt.offset(-playerAtAC.areaX, -playerAtAC.areaY);

    for (int y = 0; y < areaSize.getHeight(); y++) {
      for (int x = 0; x < areaSize.getWidth(); x++) {

        Coordinate thisCoordinate = playerAreaOrigin.offset(x, y);

        final Physical visible = world.getSquare(thisCoordinate).peek();

        final char mapSymbol = visible.getMapSymbol();
        final Color color = visible.getColor();
        final Color bgColor = visible.getBGColor();

        int placeX = (x) * tileSize;
        int placeY = (y) * tileSize;

        SquareDrawer.drawSquare(g, mapSymbol, color, bgColor, tileSize, placeX, placeY);
      }
    }

    ActionOverlay.drawOverlay((Graphics2D) g, tileSize, game);
    EventLog.drawOverlay((Graphics2D) g);
    mouseControl.drawOverlay((Graphics2D) g, playerAreaOrigin);
  }


  public int getTileSize() {
    return tileSize;
  }
}