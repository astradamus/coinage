package game.io.better_ui.widget_ui;

import game.Game;
import game.io.better_ui.GamePanel;
import game.io.better_ui.widget.AnimatedWidget;
import game.io.better_ui.widget.TextWidget;
import game.physical.Physical;
import utils.ImmutableDimension;
import utils.ImmutablePoint;
import utils.ImmutableRectangle;
import world.Coordinate;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MouseControl implements MouseMotionListener, MouseListener {

  private final Game game;

  private final int tileSize;
  private final Dimension screenSize;

  private Coordinate playerAreaOrigin;
  private MousePosition mouseAt;

  private List<ToolTip> toolTips;

  public MouseControl(Game game, GamePanel gamePanel) {
    this.game = game;
    tileSize = gamePanel.getTileSize();
    screenSize = gamePanel.getPreferredSize();
    toolTips = new ArrayList<>();
  }


  public void drawOverlay(Graphics2D g, Coordinate playerAreaOrigin) {
    this.playerAreaOrigin = playerAreaOrigin;


    if (mouseAt != null) {

      final ImmutablePoint mouseAtIP = new ImmutablePoint(mouseAt.getX(), mouseAt.getY());
      if (mouseAt.getToolTipHoverTimeReached()
          && toolTips.stream().noneMatch(tT -> mouseAtIP.equalTo(tT.point))) {
        toolTips.add(new ToolTip(mouseAtIP, makeToolTip(g, getPhysicalUnderMouse())));
      }
    }

    for (final ToolTip toolTip : toolTips) {
      toolTip.widget.draw(g);
    }
  }

  private class ToolTip {
    private final ImmutablePoint point;
    private final AnimatedWidget widget;

    final ImmutableRectangle collapsedBox;
    private boolean fading = false;


    public ToolTip(ImmutablePoint point, AnimatedWidget widget) {
      this.point = point;
      this.widget = widget;

      final ImmutableRectangle mB = widget.getMarginBox();
      collapsedBox =
          mB.getAdjusted(mB.getWidth() / 2, mB.getHeight() / 2, -mB.getWidth(), -mB.getHeight());

      this.widget.transform(collapsedBox, mB, 350);
      this.widget.fade(0, widget.getAlpha().getAlpha(), 500);
    }

    void fadeOut() {
      if (fading) {
        return;
      }
      else {
        fading = true;
      }
      final ImmutableRectangle mB = widget.getMarginBox();
      this.widget.transform(mB, collapsedBox, 275);
      this.widget.fade(widget.getAlpha().getAlpha(), 0, 300);

      final Timer timer = new Timer(300, (aE) -> toolTips.remove(this));
      timer.setRepeats(false);
      timer.start();
    }
  }


  private TextWidget makeToolTip(Graphics g, Physical p) {

    final ImmutablePoint widgetAt =
        new ImmutablePoint((mouseAt.getX() + 2) * tileSize, mouseAt.getY() * tileSize);

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
    mouseAt = new MousePosition(tileX, tileY, System.currentTimeMillis());
  }


  private void clearCursor() {
    mouseAt = null;
    toolTips.forEach(ToolTip::fadeOut);
  }


  private Physical getPhysicalUnderMouse() {
    return game.getWorld()
        .getSquare(playerAreaOrigin.offset(mouseAt.getX(), mouseAt.getY())).peek();
  }


  @Override
  public void mouseMoved(MouseEvent e) {

    final int tileX = e.getX() / tileSize;
    final int tileY = e.getY() / tileSize;

    final ImmutableDimension areaSize = game.getWorld().getAreaSizeInSquares();
    if (tileX >= areaSize.getWidth() || tileY >= areaSize.getHeight()) {
      mouseExited(e);
      return;
    }

    if (mouseAt == null || !mouseAt.equalTo(tileX, tileY)) {
      setCursor(tileX, tileY);
    }
  }


  @Override
  public void mouseClicked(MouseEvent e) { }


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