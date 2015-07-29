package game.io.better_ui;

import game.Game;
import game.physical.Physical;
import utils.Dimension;
import world.AreaCoordinate;
import world.Coordinate;
import world.World;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 */
public class GamePanel extends JPanel implements MouseMotionListener, MouseListener {

  private final Font gamePanelFont;
  private final Dimension areaSize;
  private final int tileSize;
  private final Game game;

  private final Point cursorLocation;
  private Long cursorLocationLastUpdateTime;
  private boolean shouldShowToolTip;

  private Coordinate playerAreaOrigin;
  private MouseMenu mouseMenu;


  public GamePanel(Game game, int tileSize) {
    gamePanelFont = new Font("SansSerif", Font.BOLD, tileSize);
    this.tileSize = tileSize;
    this.game = game;

    cursorLocation = new Point(-1, -1);
    setBackground(Color.black);

    areaSize = game.getWorld().getAreaSizeInSquares();
    setPreferredSize(
        new java.awt.Dimension(tileSize * areaSize.getWidth(), tileSize * areaSize.getHeight()));

    addMouseListener(this);
    addMouseMotionListener(this);

    new Timer(20, e -> repaint()).start();

    this.mouseMenu = new MouseMenu(this, tileSize);
    EventLog.initialize(game, tileSize);
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (cursorLocationLastUpdateTime != null) {
      if (System.currentTimeMillis() - cursorLocationLastUpdateTime > 400) {
        shouldShowToolTip = true;
      }
    }

    if (shouldShowToolTip) {
      final Physical physical =
          game.getWorld().getSquare(playerAreaOrigin.offset(cursorLocation.x, cursorLocation.y))
              .peek();

      Color bgColor = physical.getBGColor();
      if (bgColor == null) {
        bgColor = Color.BLACK;
      }
      mouseMenu
          .setToolTip(cursorLocation.x, cursorLocation.y, physical.getName(), physical.getColor(),
              bgColor);
    }


    g.setFont(gamePanelFont);

    final World world = game.getWorld();

    Coordinate playerAt = game.getActivePlayerActor().getCoordinate();
    AreaCoordinate playerAtAC = world.convertToAreaCoordinate(playerAt);
    playerAreaOrigin = playerAt.offset(-playerAtAC.areaX, -playerAtAC.areaY);

    for (int y = 0; y < world.getAreaSizeInSquares().getHeight(); y++) {
      for (int x = 0; x < world.getAreaSizeInSquares().getWidth(); x++) {

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
    mouseMenu.drawOverlay(g);
    EventLog.drawOverlay((Graphics2D) g);

  }


  private void setCursor(int tileX, int tileY) {
    mouseMenu.clear();
    cursorLocation.setLocation(tileX, tileY);
    cursorLocationLastUpdateTime = System.currentTimeMillis();
    shouldShowToolTip = false;
  }


  private void clearCursor() {
    mouseMenu.clear();
    cursorLocation.setLocation(-1, -1);
    cursorLocationLastUpdateTime = null;
    shouldShowToolTip = false;
  }


  @Override
  public void mouseClicked(MouseEvent e) {

  }


  @Override
  public void mousePressed(MouseEvent e) {

  }


  @Override
  public void mouseReleased(MouseEvent e) {

  }


  @Override
  public void mouseEntered(MouseEvent e) {

  }


  @Override
  public void mouseExited(MouseEvent e) {
    clearCursor();
  }


  @Override
  public void mouseDragged(MouseEvent e) {

  }


  @Override
  public void mouseMoved(MouseEvent e) {
    final int tileX = e.getX() / tileSize;
    final int tileY = e.getY() / tileSize;

    if (tileX >= areaSize.getWidth() || tileY >= areaSize.getHeight()) {
      mouseExited(e);
      return;
    }

    if (cursorLocation.x != tileX || cursorLocation.y != tileY) {
      setCursor(tileX, tileY);
    }
  }
}