package game.io.better_ui.widget_ui;

import utils.ImmutablePoint;

/**
 *
 */
public class MousePosition extends ImmutablePoint {

  private final long updateTime;
  private boolean toolTipHoverTimeReached;


  public MousePosition(int x, int y, long updateTime) {
    super(x, y);
    this.updateTime = updateTime;
  }


  public boolean getToolTipHoverTimeReached() {
    if (toolTipHoverTimeReached) {
      return true;
    }

    toolTipHoverTimeReached = System.currentTimeMillis() - updateTime > 200;
    return toolTipHoverTimeReached;
  }
}