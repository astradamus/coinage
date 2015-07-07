package world;

/**
 *
 */
public class MapCoordinate {

  public final int worldAreasX;
  public final int worldAreasY;

  MapCoordinate(int worldAreasX, int worldAreasY) {
    this.worldAreasX = worldAreasX;
    this.worldAreasY = worldAreasY;
  }

  /**
   * @return The Chebyshev/"Chessboard" distance between this and another coordinate.
   */
  public int getDistance(MapCoordinate target) {

    final int deltaX = Math.abs(this.worldAreasX-target.worldAreasX);
    final int deltaY = Math.abs(this.worldAreasY-target.worldAreasY);

    return Math.max(deltaX, deltaY);

  }

}