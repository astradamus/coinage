package game.io.better_ui.widget_ui;

/**
 *
 */
public class MousePosition {

  private final int x;
  private final int y;

  private final long updateTime;
  private boolean toolTipHoverTimeReached;


  public MousePosition(int x, int y, long updateTime) {
    this.x = x;
    this.y = y;
    this.updateTime = updateTime;
  }


  public boolean equalTo(int x, int y) {
    return this.x == x && this.y == y;
  }


  public int getX() {
    return x;
  }


  public int getY() {
    return y;
  }


  public boolean getToolTipHoverTimeReached() {
    if (toolTipHoverTimeReached) {
      return true;
    }

    toolTipHoverTimeReached = System.currentTimeMillis() - updateTime > 400;
    return toolTipHoverTimeReached;
  }
}