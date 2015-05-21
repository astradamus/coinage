package controller;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Direction {

  NORTH       (0,-1),
  NORTH_EAST  (1,-1),
  EAST        (1,0),
  SOUTH_EAST  (1,1),
  SOUTH       (0,1),
  SOUTH_WEST  (-1,1),
  WEST        (-1,0),
  NORTH_WEST  (-1,-1);

  public final int relativeX;
  public final int relativeY;

  Direction(int relativeX, int relativeY) {
    this.relativeX = relativeX;
    this.relativeY = relativeY;
  }

  public static Direction fromKeyEvent(KeyEvent keyEvent) {
    switch (keyEvent.getKeyCode()) {
      case KeyEvent.VK_HOME:      return NORTH_WEST;
      case KeyEvent.VK_UP:        return NORTH;
      case KeyEvent.VK_PAGE_UP:   return NORTH_EAST;
      case KeyEvent.VK_RIGHT:     return EAST;
      case KeyEvent.VK_PAGE_DOWN: return SOUTH_EAST;
      case KeyEvent.VK_DOWN:      return SOUTH;
      case KeyEvent.VK_END:       return SOUTH_WEST;
      case KeyEvent.VK_LEFT:      return WEST;
      default: return null;
    }
  }
  
}