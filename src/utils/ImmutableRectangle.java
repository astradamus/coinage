package utils;

import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 */
public class ImmutableRectangle extends ImmutableDimension {

  private final int x;
  private final int y;


  public ImmutableRectangle(int x, int y, int width, int height) {
    super(width, height);
    this.x = x;
    this.y = y;
  }


  public ImmutableRectangle(Rectangle awtRectangle) {
    super(awtRectangle.width, awtRectangle.height);
    x = awtRectangle.x;
    y = awtRectangle.y;
  }


  public ImmutableRectangle(ImmutableDimension immutableDimension) {
    super(immutableDimension.getWidth(), immutableDimension.getHeight());
    x = 0;
    y = 0;
  }


  public ImmutableRectangle(ImmutablePoint originPoint, int width, int height) {
    this(originPoint.getX(), originPoint.getY(), width, height);
  }


  public ImmutableRectangle(ImmutablePoint origin, ImmutableDimension size) {
    this(origin, size.getWidth(), size.getHeight());
  }


  @Override
  public boolean contains(int x, int y) {
    return (x >= getLeft() && x <= getRight() && y >= getTop() && y <= getBottom());
  }


  @Override
  public boolean contains(Point point) {
    return contains(point.x, point.y);
  }


  public boolean contains(int x, int y, int width, int height) {
    return contains(x, y) && contains(x + width, y + height);
  }


  public boolean contains(ImmutableRectangle rect) {
    return contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }


  public boolean contains(Rectangle rect) {
    return contains(rect.x, rect.y, rect.width, rect.height);
  }


  public ImmutableRectangle getAdjusted(int adjX, int adjY, int adjWidth, int adjHeight) {
    return new ImmutableRectangle(getX() + adjX, getY() + adjY, getWidth() + adjWidth,
        getHeight() + adjHeight);
  }


  public ImmutablePoint getOrigin() {
    return new ImmutablePoint(x, y);
  }


  public int getX() {
    return x;
  }


  public int getY() {
    return y;
  }


  public ImmutablePoint getCenter() {
    return new ImmutablePoint(getCenterX(), getCenterY());
  }


  public int getCenterX() {
    return x + getWidth() / 2;
  }


  public int getCenterY() {
    return y + getHeight() / 2;
  }


  public int getLeft() {
    return getX();
  }


  public int getTop() {
    return getY();
  }


  public int getRight() {
    return getX() + getWidth();
  }


  public int getBottom() {
    return getY() + getHeight();
  }


  public Rectangle toRectangle() {
    return new Rectangle(x, y, getWidth(), getHeight());
  }


  @Override
  public String toString() {
    return "ImmutableRectangle[x: " + Integer.toString(x) + ", y: " + Integer.toString(y)
        + ", width: " + Integer.toString(getWidth()) + ", height: " + Integer.toString(getHeight())
        + "]";
  }
}