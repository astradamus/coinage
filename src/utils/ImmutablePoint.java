package utils;

import java.awt.Point;

/**
 *
 */
public class ImmutablePoint {

  private final int x;
  private final int y;


  public ImmutablePoint(int x, int y) {
    this.x = x;
    this.y = y;
  }


  public ImmutablePoint(Point awtPoint) {
    x = awtPoint.x;
    y = awtPoint.y;
  }


  public boolean equalTo(int x, int y) {
    return this.x == x && this.y == y;
  }


  public boolean equalTo(ImmutablePoint point) {
    return equalTo(point.x, point.y);
  }


  public boolean equalTo(Point awtPoint) {
    return equalTo(awtPoint.x, awtPoint.y);
  }


  public ImmutablePoint getTranslated(int adjX, int adjY) {
    return new ImmutablePoint(x + adjX, y + adjY);
  }


  public ImmutablePoint getTranslated(ImmutablePoint point) {
    return getTranslated(point.getX(), point.getY());
  }


  public int getX() {
    return x;
  }


  public int getY() {
    return y;
  }

  public Point toPoint() {
    return new Point(x, y);
  }
}