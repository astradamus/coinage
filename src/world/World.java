package world;

import game.Physical;
import utils.Dimension;

import java.awt.*;

/**
 *
 */
public class World {

  private final Area[][] areas;

  private final Dimension worldSizeInAreas;
  private final Dimension areaSizeInSquares;
  private final Dimension globalSizeInSquares;

  World(Area[][] areas) {

    this.areas = areas;

    this.worldSizeInAreas = new Dimension(areas[0].length,areas.length);
    this.areaSizeInSquares = areas[0][0].getSize();

    int worldWidthInSquares = worldSizeInAreas.getWidth()*areaSizeInSquares.getWidth();
    int worldHeightInSquares = worldSizeInAreas.getHeight()*areaSizeInSquares.getHeight();

    this.globalSizeInSquares = new Dimension(worldWidthInSquares,worldHeightInSquares);

  }


  /**
   * Breaks the given global coordinate (numbered in squares) and hands off the results to the
   * found Area to put the given physical to the list for the found local coordinate.
   */
  public void put(Physical putting, int globalX, int globalY) {
    BreakResult bR = breakGlobalCoordinate(globalX, globalY);
    bR.area.getPhysicalsComponent().put(putting, bR.localX, bR.localY);
  }

  /**
   * Breaks the given global coordinates (numbered in squares), attempts to pull the given
   * physical from the broken "from" coordinate and, if successful, puts that physical to the
   * broken "to" coordinate.
   */
  public boolean move(Physical moving, int fromGlobalX, int fromGlobalY,
                      int toGlobalX, int toGlobalY) {

    if (!globalSizeInSquares.getCoordinateIsWithinBounds(toGlobalX,toGlobalY)) {
      return false; // don't allow movement to points outside the world
    }
    
    BreakResult from = breakGlobalCoordinate(fromGlobalX, fromGlobalY);
    BreakResult to = breakGlobalCoordinate(toGlobalX, toGlobalY);

    if (to.area.getPhysicalsComponent().getCoordinateIsBlocked(to.localX, to.localY)) {
      return false;
    }
    if (from.area.getPhysicalsComponent().pull(moving, from.localX, from.localY)) {
      to.area.getPhysicalsComponent().put(moving, to.localX, to.localY);
      return true;
    } else {
      System.out.println("Attempted to move Physical that was not found at fromGlobalX/fromGlobalY.");
      return false;
    }

  }


  /**
   * Translates a global coordinate (numbered in squares) into an Area.
   */
  public Area getAreaByGlobalCoordinate(int globalX, int globalY) {
    return breakGlobalCoordinate(globalX, globalY).area;
  }

  /**
   * Translates a world coordinate (numbered in Areas) into an Area.
   */
  public Area getAreaByWorldCoordinate(int worldX, int worldY) {
    if (worldSizeInAreas.getCoordinateIsWithinBounds(worldX, worldY)) {
      return areas[worldY][worldX];
    } else {
      return null;
    }
  }

  /**
   * Translates a global coordinate (numbered in squares) into a World coordinate (measured in
   * Areas).
   */
  public Point getWorldCoordinateFromGlobalCoordinate(Point globalCoordinate) {
    if (globalSizeInSquares.getCoordinateIsWithinBounds(globalCoordinate.x, globalCoordinate.y)) {
      return new Point(globalCoordinate.x / areaSizeInSquares.getWidth(),
          globalCoordinate.y / areaSizeInSquares.getHeight());
    } else {
      return null;
    }
  }

  /**
   * Translates a global coordinate (numbered in squares) into an Area coordinate (measured in
   * squares).
   */
  public Point getAreaCoordinateFromGlobalCoordinate(Point globalCoordinate) {
    if (globalSizeInSquares.getCoordinateIsWithinBounds(globalCoordinate.x, globalCoordinate.y)) {
      return new Point(globalCoordinate.x % areaSizeInSquares.getWidth(),
          globalCoordinate.y % areaSizeInSquares.getHeight());
    } else {
      return null;
    }
  }

  public Dimension getAreaSizeInSquares() {
    return areaSizeInSquares;
  }

  public Dimension getGlobalSizeInSquares() {
    return globalSizeInSquares;
  }


  /**
   * Translates a global coordinate into an Area and local coordinates within that area.
   */
  private BreakResult breakGlobalCoordinate(int worldX, int worldY) {

    int areaWidth = areaSizeInSquares.getWidth();
    int areaHeight = areaSizeInSquares.getHeight();

    Area area = areas[worldY / areaHeight][worldX / areaWidth];
    int localX = worldX % areaWidth;
    int localY = worldY % areaHeight;

    return new BreakResult(area,localX,localY);

  }


  private class BreakResult {
    public final Area area;
    public final int localX;
    public final int localY;

    public BreakResult(Area area, int localX, int localY) {
      this.area = area;
      this.localX = localX;
      this.localY = localY;
    }
  }

}
