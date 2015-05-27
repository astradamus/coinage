package utils;

import java.awt.*;

/**
 * An immutable replacement for Java.awt.Dimension.
 */
public class Dimension {

  private final int width;
  private final int height;

  public Dimension(int width, int height) {
    this.width = width;
    this.height = height;
  }


  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean getCoordinateIsWithinBounds(int testX, int testY) {
    return (testX >= 0 && testX < width && testY >= 0 && testY < height);
  }

  public boolean getCoordinateIsWithinBounds(Point localCoordinate) {
    return getCoordinateIsWithinBounds(localCoordinate.x, localCoordinate.y);
  }

}
