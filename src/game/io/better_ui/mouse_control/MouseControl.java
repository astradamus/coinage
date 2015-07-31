package game.io.better_ui.mouse_control;

import actor.Actor;
import game.Game;
import game.io.better_ui.GamePanel;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import utils.ImmutableDimension;
import world.Coordinate;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 */
public class MouseControl implements MouseMotionListener, MouseListener {

  private final Game game;

  private final int tileSize;
  private final Font mouseMenuFont;
  private final MouseMenuFactory menuFactory;

  private Coordinate playerAreaOrigin;
  private MousePosition mousePosition;
  private MouseMenu mouseMenu;


  public MouseControl(Game game, GamePanel gamePanel) {
    this.game = game;
    tileSize = gamePanel.getTileSize();
    mouseMenuFont = new Font("Monospaced", Font.BOLD, tileSize);

    menuFactory = new MouseMenuFactory(tileSize, new Rectangle(gamePanel.getPreferredSize()),
        gamePanel.getFontMetrics(mouseMenuFont));
  }


  public void drawOverlay(Graphics g, Coordinate playerAreaOrigin) {
    this.playerAreaOrigin = playerAreaOrigin;

    if (mouseMenu == null || mouseMenu.allowEasyReplacement()) {
      if (mousePosition != null && mousePosition.getToolTipHoverTimeReached()) {
        mouseMenu = menuFactory.makeToolTip(mousePosition, getPhysicalUnderMouse());
      }
    }

    if (mouseMenu != null) {
      mouseMenu.drawOverlay(g);
    }
  }


  private void setCursor(int tileX, int tileY) {
    mousePosition = new MousePosition(tileX, tileY, System.currentTimeMillis());
    mouseMenu = null;
  }


  private void clearCursor() {
    mousePosition = null;
    mouseMenu = null;
  }


  private Physical getPhysicalUnderMouse() {
    return game.getWorld()
        .getSquare(playerAreaOrigin.offset(mousePosition.getX(), mousePosition.getY())).peek();
  }


  @Override
  public void mouseDragged(MouseEvent e) {
    if (mouseMenu != null) {
      mouseMenu.mouseDragged(e);
    }
  }


  @Override
  public void mouseMoved(MouseEvent e) {
    if (mouseMenu != null && mouseMenu.mouseMoved(e)) {
      return; // Mouse menu handled event, do nothing else.
    }

    final int tileX = e.getX() / tileSize;
    final int tileY = e.getY() / tileSize;

    final ImmutableDimension areaSize = game.getWorld().getAreaSizeInSquares();
    if (tileX >= areaSize.getWidth() || tileY >= areaSize.getHeight()) {
      mouseExited(e);
      return;
    }

    if (mousePosition == null || !mousePosition.equalTo(tileX, tileY)) {
      setCursor(tileX, tileY);
    }
  }


  @Override
  public void mouseClicked(MouseEvent e) {
    if (mouseMenu != null) {
      mouseMenu.mouseClicked(e);
    }

    final Physical physical = getPhysicalUnderMouse();
    if (physical.getClass() == Actor.class && !physical.hasFlag(PhysicalFlag.DEAD)) {
      mouseMenu = menuFactory.makeToolBox(mousePosition, (Actor) physical);
    }
  }


  @Override
  public void mousePressed(MouseEvent e) {
    if (mouseMenu != null) {
      mouseMenu.mousePressed(e);
    }
  }


  @Override
  public void mouseReleased(MouseEvent e) {
    if (mouseMenu != null) {
      mouseMenu.mouseReleased(e);
    }
  }


  @Override
  public void mouseEntered(MouseEvent e) {
    if (mouseMenu != null) {
      mouseMenu.mouseEntered(e);
    }
  }


  @Override
  public void mouseExited(MouseEvent e) {
    if (mouseMenu != null) {
      mouseMenu.mouseExited(e);
    }
    clearCursor();
  }
}