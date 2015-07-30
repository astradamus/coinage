package game.io.better_ui.mouse_control;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 *
 */
public abstract class MouseMenu {

  private Rectangle mouseMenuBounds = new Rectangle(0, 0, 0, 0);


  protected void drawOverlay(Graphics g) { }


  protected boolean handleMouseMoved(MouseEvent e) { return false; }
  protected boolean handleMouseDragged(MouseEvent e) { return false; }
  protected boolean handleMouseClicked(MouseEvent e) { return false; }
  protected boolean handleMousePressed(MouseEvent e) { return false; }
  protected boolean handleMouseReleased(MouseEvent e) { return false; }
  protected boolean handleMouseEntered(MouseEvent e) { return false; }
  protected boolean handleMouseExited(MouseEvent e) { return false; }

  public final boolean mouseMoved(MouseEvent e) {
    return mouseMenuBounds.contains(e.getX(), e.getY()) && handleMouseMoved(e);
  }


  public final boolean mouseDragged(MouseEvent e) {
    return mouseMenuBounds.contains(e.getX(), e.getY()) && handleMouseDragged(e);
  }


  public final boolean mouseClicked(MouseEvent e) {
    return mouseMenuBounds.contains(e.getX(), e.getY()) && handleMouseClicked(e);
  }


  public final boolean mousePressed(MouseEvent e) {
    return mouseMenuBounds.contains(e.getX(), e.getY()) && handleMousePressed(e);
  }


  public final boolean mouseReleased(MouseEvent e) {
    return mouseMenuBounds.contains(e.getX(), e.getY()) && handleMouseReleased(e);
  }


  public final boolean mouseEntered(MouseEvent e) {
    return mouseMenuBounds.contains(e.getX(), e.getY()) && handleMouseEntered(e);
  }


  public final boolean mouseExited(MouseEvent e) {
    return mouseMenuBounds.contains(e.getX(), e.getY()) && handleMouseExited(e);
  }


  protected void setMouseMenuBounds(int tileSize, Rectangle containerBounds, int x, int y,
      int width, int height) {

    int adjustedX = x;
    int adjustedY = y;
    final int adjustedWidth = width + getMouseMenuPadding().width;
    final int adjustedHeight = height + getMouseMenuPadding().height;

    if (adjustedX + adjustedWidth > containerBounds.x + containerBounds.width) {
      adjustedX = adjustedX - adjustedWidth - tileSize * 3;
    }
    if (adjustedY + adjustedHeight > containerBounds.y + containerBounds.height) {
      adjustedY -= adjustedHeight;
    }
    if (adjustedX < containerBounds.x) {
      adjustedX = containerBounds.x;
    }
    if (adjustedY < containerBounds.y) {
      adjustedY = containerBounds.y;
    }

    this.mouseMenuBounds = new Rectangle(adjustedX, adjustedY, adjustedWidth, adjustedHeight);
  }


  protected Dimension getMouseMenuPadding() {
    return new Dimension(0, 0);
  }


  protected Rectangle getMouseMenuBounds() {
    return new Rectangle(mouseMenuBounds);
  }


  public boolean allowEasyReplacement() {
    return true;
  }


}