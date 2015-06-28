package world;

import game.Direction;
import utils.Utils;

/**
 *
 */
public class Coordinate {

  public final int globalX;
  public final int globalY;

  public final int worldX;
  public final int worldY;

  public final Area area;

  public final int localX;
  public final int localY;

  Coordinate(int globalX, int globalY, int worldX, int worldY, Area area, int localX, int localY) {
    this.globalX = globalX;
    this.globalY = globalY;
    this.worldX = worldX;
    this.worldY = worldY;
    this.area = area;
    this.localX = localX;
    this.localY = localY;
  }

  /**
   * @return The Chebyshev/"Chessboard" distance between this and another coordinate, with units
   * measured in individual game squares.
   */
  public int getGlobalDistance(Coordinate target) {

    final int deltaX = Math.abs(this.globalX-target.globalX);
    final int deltaY = Math.abs(this.globalY-target.globalY);

    return Math.max(deltaX, deltaY);

  }

  /**
   * @return The Chebyshev/"Chessboard" distance between this and another coordinate, with units
   * measured in whole game areas.
   */
  public int getWorldDistance(Coordinate target) {

    final int deltaX = Math.abs(this.worldX-target.worldX);
    final int deltaY = Math.abs(this.worldY-target.worldY);

    return Math.max(deltaX, deltaY);

  }

  /**
   * @return The direction from this to another coordinate. Only due north is considered north--one
   * square west or east becomes northwest or northeast, and so on for each other direction. This
   * means the range considered "northwest" is a lot bigger than that considered "north" or "west"
   * individually.
   */
  public Direction getDirectionTo(Coordinate target) {
    return Direction.fromPointToPoint(this.globalX, this.globalY, target.globalX, target.globalY);
  }

  public boolean getIsAdjacentTo(Coordinate target) {
    return Utils.getPointsAreAdjacent(
        this.globalX, this.globalY,
        target.globalX, target.globalY);
  }



  public Square getSquare() {
    return area.getSquare(this);
  }

  public boolean equalTo(Coordinate coordinate) {
    return globalX == coordinate.globalX && globalY == coordinate.globalY;
  }

}