package game.io.better_ui.widget_ui;

import game.Game;
import game.io.better_ui.GamePanel;
import game.io.better_ui.widget.AnimatedWidget;
import game.io.better_ui.widget.Button;
import game.io.better_ui.widget.LinearLayout;
import game.io.better_ui.widget.Orientation;
import game.io.better_ui.widget.TextWidget;
import game.io.better_ui.widget.Widget;
import game.physical.Physical;
import utils.ImmutableDimension;
import utils.ImmutablePoint;
import utils.ImmutableRectangle;
import world.Coordinate;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 */
public class MouseControl implements KeyListener, MouseMotionListener, MouseListener {

  private final Game game;
  private final int tileSize;
  private final Dimension screenSize;
  private GamePanel gamePanel;
  private Coordinate playerAreaOrigin;
  private MousePosition mouseAt;

  private ToolTip toolTip;
  private boolean ctrlIsDown;
  private boolean shiftIsDown;


  public MouseControl(Game game, GamePanel gamePanel) {
    this.game = game;
    this.gamePanel = gamePanel;
    tileSize = gamePanel.getTileSize();
    screenSize = gamePanel.getPreferredSize();
  }


  public void drawOverlay(Graphics2D g, Coordinate playerAreaOrigin) {
    this.playerAreaOrigin = playerAreaOrigin;

    if (mouseAt != null) {
      if (ctrlIsDown) {
        g.setColor(Color.RED);
        g.drawOval(mouseAt.getX() * tileSize, mouseAt.getY() * tileSize, tileSize, tileSize);
      }
      else if (shiftIsDown) {
        g.setColor(Color.GREEN);
        g.drawOval(mouseAt.getX() * tileSize, mouseAt.getY() * tileSize, tileSize, tileSize);
      }
    }

    if (toolTip == null && mouseAt != null && mouseAt.getToolTipHoverTimeReached()) {
      toolTip = new ToolTip();
      toolTip.fadeIn();
    }

    if (toolTip != null) {

      if (toolTip.handlesMouse && mouseAt != null) {
        g.setColor(Color.WHITE);
        g.drawOval(mouseAt.getX() * tileSize, mouseAt.getY() * tileSize, tileSize, tileSize);
      }

      toolTip.draw(g);
    }
  }


  private void setCursor(int tileX, int tileY) {
    clearCursor();
    mouseAt = new MousePosition(tileX, tileY, System.currentTimeMillis());
  }


  private void clearCursor() {
    mouseAt = null;
    if (toolTip != null) {
      toolTip.fadeOut();
    }
  }


  private Physical getPhysicalUnderMouse() {
    return game.getWorld().getSquare(playerAreaOrigin.offset(mouseAt.getX(), mouseAt.getY()))
        .peek();
  }


  @Override
  public void mouseClicked(MouseEvent e) {
    if (toolTip != null && toolTip.handlesMouse) {
      if (toolTip.getMarginBox().contains(e.getPoint())) {
        toolTip.handleMouseClicked(e);
      }
      else {
        clearCursor();
      }
      return;
    }

    if (toolTip == null) {
      if (mouseAt == null) {
        mouseMoved(e); // Because cursor is cleared after a delay, mouseAt can sometimes be null.
        // This solution is ugly and bug prone, as is this entire implementation. Fix it!
      }
      toolTip = new ToolTip();
      toolTip.resize(new ImmutableDimension(0, toolTip.getMarginBox().getHeight()));
    }

    if (ctrlIsDown) {
      onClickAttackButton();
      return;
    }
    else if (shiftIsDown) {
      onClickPickUpButton();
      return;
    }

    toolTip.handlesMouse = true;
    toolTip.setBorder(toolTip.getChild(0).getBorder());
    toolTip.setBorderColor(toolTip.getChild(0).getBorderColor());
    final ImmutableRectangle mB = toolTip.getMarginBox();

    final ImmutableRectangle box;
    final int width = Math.max(242, mB.getWidth());
    final ImmutableRectangle newBox =
        new ImmutableRectangle(mB.getX(), mB.getY(), width, mB.getHeight());

    int adjX = 0;

    if (newBox.getRight() > screenSize.width) {
      adjX = -newBox.getWidth() - tileSize * 3;
    }

    box = newBox.getAdjusted(adjX, 0, 0, 0);

    final LinearLayout tools = new LinearLayout(null, Orientation.HORZ);

    final FontMetrics fM = gamePanel.getFontMetrics(TextWidget.STANDARD_FONT);

    final Color red = new Color(100, 0, 0, 225).darker().darker();
    final Color green = new Color(0, 100, 0, 225).darker().darker();
    final Color blue = new Color(0, 0, 100, 225).darker().darker();

    final ToolBoxButton pickUpButton = new ToolBoxButton(green, fM, "Pick\nUp");
    pickUpButton.setOnClick(this::onClickPickUpButton);
    tools.add(pickUpButton);

    final ToolBoxButton attackButton = new ToolBoxButton(red, fM, "Attack\nHere");
    attackButton.setOnClick(this::onClickAttackButton);
    tools.add(attackButton);

    tools.add(new ToolBoxButton(blue, fM, "Use\nSkill"));

    tools.setLayoutWeight(1);
    toolTip.animateTransform(mB, box, 150);

    toolTip.add(tools);

    final Timer timer = new Timer(150, e1 -> {
      final ImmutableRectangle nextBox =
          new ImmutableRectangle(box.getX(), box.getY(), box.getWidth(), 75);
      final ImmutableRectangle next;

      int adjY = 0;
      if (nextBox.getBottom() > screenSize.height) {
        adjY = screenSize.height - nextBox.getBottom();
      }

      next = nextBox.getAdjusted(0, adjY, 0, 0);

      toolTip.animateTransform(toolTip.getMarginBox(), next, 150);
    });
    timer.setRepeats(false);
    timer.start();
  }


  private void onClickPickUpButton() {
    Commands.playerPickUp(gamePanel.getGame(),
        playerAreaOrigin.offset(mouseAt.getX(), mouseAt.getY()));

    final Timer timer = new Timer(250, (aE) -> clearCursor());
    timer.setRepeats(false);
    timer.start();
  }


  private void onClickAttackButton() {
    Commands.playerAttack(gamePanel.getGame(),
        playerAreaOrigin.offset(mouseAt.getX(), mouseAt.getY()));

    final Timer timer = new Timer(250, (aE) -> clearCursor());
    timer.setRepeats(false);
    timer.start();
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


  @Override
  public void mouseMoved(MouseEvent e) {

    if (toolTip != null && toolTip.handlesMouse) {
      toolTip.handleMouseMoved(e);
      return;
    }

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
  public void keyTyped(KeyEvent e) {

  }


  @Override
  public void keyPressed(KeyEvent e) {
    ctrlIsDown = e.isControlDown();
    shiftIsDown = e.isShiftDown();
  }


  @Override
  public void keyReleased(KeyEvent e) {
    ctrlIsDown = e.isControlDown();
    shiftIsDown = e.isShiftDown();
  }


  class ToolTip extends LinearLayout {
    final ImmutableRectangle collapsedBox;

    private boolean fading = false;
    private boolean handlesMouse = false;


    public ToolTip() {
      super(null, Orientation.VERT);

      final ImmutablePoint widgetAt =
          new ImmutablePoint((mouseAt.getX() + 2) * tileSize, mouseAt.getY() * tileSize);

      final Physical p = getPhysicalUnderMouse();

      final TextWidget titleWidget =
          new TextWidget(gamePanel.getFontMetrics(TextWidget.STANDARD_FONT), p.getName());

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
      moveAndResize(marginBox);
      add(titleWidget);

      int offX = 0;
      int offY = 0;

      if (marginBox.getRight() > screenSize.getWidth()) {
        offX = -tileSize * 3 - marginBox.getWidth();
      }
      if (marginBox.getBottom() > screenSize.getHeight()) {
        offY = -marginBox.getHeight();
      }

      if (offX != 0 || offY != 0) {
        move(marginBox.getOrigin().getTranslated(offX, offY));
      }

      final ImmutableRectangle mB = getMarginBox();
      collapsedBox =
          mB.getAdjusted(mB.getWidth() / 2, mB.getHeight() / 2, -mB.getWidth(), -mB.getHeight());
    }


    void fadeIn() {
      final AnimatedWidget child = (AnimatedWidget) this.getChild(0);
      child.animateTransform(collapsedBox, getMarginBox(), 350);
      child.animateFade(0, getAlpha().getAlpha(), 500);
    }


    void fadeOut() {
      if (fading) {
        return;
      }
      else {
        fading = true;
      }

      for (Widget child : this.getSubwidgets()) {
        final AnimatedWidget anim = (AnimatedWidget) child;
        final ImmutableRectangle mB = anim.getMarginBox();
        anim.animateTransform(mB, collapsedBox, 275);
        anim.animateFade(child.getAlpha().getAlpha(), 0, 300);
      }

      this.animateTransform(this.getMarginBox(), collapsedBox, 275);
      this.animateFade(this.getAlpha().getAlpha(), 0, 300);

      final Timer timer = new Timer(300, (aE) -> toolTip = null);
      timer.setRepeats(false);
      timer.start();
    }
  }
  class ToolBoxButton extends Button {
    public ToolBoxButton(Color bgColor, FontMetrics fontMetrics, String string) {
      super(fontMetrics, string);
      setLayoutWeight(1);
      setBorder(2);
      setBorderColor(bgColor);
      setBgColor(bgColor.darker());
    }
  }
}