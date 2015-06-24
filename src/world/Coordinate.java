package world;

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

  public Square getSquare() {
    return area.getSquare(this);
  }

  public boolean equalTo(Coordinate coordinate) {
    return globalX == coordinate.globalX && globalY == coordinate.globalY;
  }

}
