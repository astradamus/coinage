package utils;

import java.awt.Dimension;
import java.awt.Point;

/**
 *
 */
public class ImmutableDimension {

  private final int width;
  private final int height;


  public ImmutableDimension(int width, int height) {
    this.width = width;
    this.height = height;
  }


  public ImmutableDimension(Dimension awtDimension) {
    this.width = awtDimension.width;
    this.height = awtDimension.height;
  }


  public boolean contains(int testX, int testY) {
    return (testX >= 0 && testX < width && testY >= 0 && testY < height);
  }


  public boolean contains(ImmutablePoint testPoint) {
    return contains(testPoint.getX(), testPoint.getY());
  }


  public boolean contains(Point testPoint) {
    return contains(testPoint.x, testPoint.y);
  }


  public int getWidth() {
    return width;
  }


  public int getHeight() {
    return height;
  }


  public int getArea() {
    return width * height;
  }


  public Dimension toDimension() {
    return new Dimension(width, height);
  }
}