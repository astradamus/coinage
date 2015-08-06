package game.io.better_ui.widget_ui;

import game.Game;
import game.io.better_ui.GamePanel;
import game.io.better_ui.widget.Fader;
import game.io.better_ui.widget.TextWidget;
import game.io.better_ui.widget.Widget;
import game.physical.Physical;
import utils.ImmutableDimension;
import utils.ImmutablePoint;
import utils.ImmutableRectangle;
import world.Coordinate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 */
public class MouseControl implements MouseMotionListener, MouseListener {

  private final Game game;

  private final int tileSize;
  private final Dimension screenSize;

  private Coordinate playerAreaOrigin;
  private MousePosition mousePosition;

  private Widget widget;
  private boolean looseWidget;
  private Fader fader;

  public MouseControl(Game game, GamePanel gamePanel) {
    this.game = game;
    tileSize = gamePanel.getTileSize();
    screenSize = gamePanel.getPreferredSize();
  }


  public void drawOverlay(Graphics2D g, Coordinate playerAreaOrigin) {
    this.playerAreaOrigin = playerAreaOrigin;

    if ((widget == null) && mousePosition != null && mousePosition.getToolTipHoverTimeReached()) {
      this.widget = makeToolTip(g, getPhysicalUnderMouse());
      looseWidget = true;

      final Runnable onComplete = () -> fader = null;
      fader = new Fader(widget, Fader.Type.IN, onComplete).start();

    }

    if (widget != null) {
      widget.draw(g);
    }
  }


  private TextWidget makeToolTip(Graphics g, Physical p) {

    final ImmutablePoint widgetAt =
        new ImmutablePoint((mousePosition.getX() + 2) * tileSize, mousePosition.getY() * tileSize);

    final TextWidget titleWidget =
        new TextWidget(g.getFontMetrics(TextWidget.STANDARD_FONT), p.getName());

    titleWidget.setBorder(1);
    titleWidget.setBorderColor(p.getColor());

    titleWidget.setColor(p.getColor());
    titleWidget.setBgColor(new Color(0, 0, 0, 255));

    titleWidget.getPadding().set(5, 2);

    titleWidget.pack();
    titleWidget.moveAndResize(new ImmutableRectangle(
        widgetAt.getTranslated(0, -titleWidget.getNonContentSize().getHeight()),
        titleWidget.getPreferredSize()));

    final ImmutableRectangle marginBox = titleWidget.getMarginBox();

    int offX = 0;
    int offY = 0;

    if (marginBox.getRight() > screenSize.getWidth()) {
      offX = -tileSize * 3 - marginBox.getWidth();
    }
    if (marginBox.getBottom() > screenSize.getHeight()) {
      offY = -marginBox.getHeight();
    }

    if (offX != 0 || offY != 0) {
      titleWidget.move(marginBox.getOrigin().getTranslated(offX, offY));
    }

    return titleWidget;
  }


  private void setCursor(int tileX, int tileY) {
    clearCursor();
    mousePosition = new MousePosition(tileX, tileY, System.currentTimeMillis());
  }


  private void clearCursor() {
    mousePosition = null;
    if ((fader == null || fader.getType() == Fader.Type.OUT) && widget != null && looseWidget) {

      if (fader != null) {
        fader.interrupt();
      }

      fader = new Fader(widget, Fader.Type.OUT, () -> {
        widget = null;
        fader = null;
      }).start();

    }
  }


  private Physical getPhysicalUnderMouse() {
    return game.getWorld()
        .getSquare(playerAreaOrigin.offset(mousePosition.getX(), mousePosition.getY())).peek();
  }


  @Override
  public void mouseMoved(MouseEvent e) {
    if (widget != null && !looseWidget) {
      widget.handleMouseMoved(e);
      return;
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
    if (widget != null && !looseWidget) {
      widget.handleMouseClicked(e);
    }
  }


  @Override
  public void mousePressed(MouseEvent e) { }


  @Override
  public void mouseReleased(MouseEvent e) { }


  @Override
  public void mouseEntered(MouseEvent e) { }


  @Override
  public void mouseExited(MouseEvent e) {
    clearCursor();
  }


  @Override
  public void mouseDragged(MouseEvent e) { }
}