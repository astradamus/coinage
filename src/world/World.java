package world;

import game.Game;
import game.Physical;
import utils.Dimension;

/**
 *
 */
public class World {

  private final Area[][] areas;

  private final Dimension worldSizeInAreas;
  private final Dimension areaSizeInSquares;
  private final Dimension globalSizeInSquares;

  World(Area[][] areas, Dimension areaSizeInSquares) {

    this.areas = areas;

    this.worldSizeInAreas = new Dimension(areas[0].length,areas.length);
    this.areaSizeInSquares = areaSizeInSquares;

    int worldWidthInSquares = worldSizeInAreas.getWidth()*areaSizeInSquares.getWidth();
    int worldHeightInSquares = worldSizeInAreas.getHeight()*areaSizeInSquares.getHeight();

    this.globalSizeInSquares = new Dimension(worldWidthInSquares,worldHeightInSquares);

  }

  public void put(Physical putting, Coordinate coordinate) {
    coordinate.getSquare().put(putting);
  }

  public boolean move(Physical moving, Coordinate from, Coordinate to) {

    if (to.getSquare().isBlocked()) {
      return false;
    }
    if (from.getSquare().pull(moving)) {
      to.getSquare().put(moving);
      return true;
    } else {
      return false;
    }

  }


  private Coordinate makeCoordinate(int globalX, int globalY) {
    if (!globalSizeInSquares.getCoordinateIsWithinBounds(globalX,globalY)) {
      return null;
    }

    final int worldX = globalX / areaSizeInSquares.getWidth();
    final int worldY = globalY / areaSizeInSquares.getHeight();

    final Area area = areas[worldY][worldX];

    final int localX = globalX % areaSizeInSquares.getWidth();
    final int localY = globalY % areaSizeInSquares.getHeight();

    return new Coordinate(globalX,globalY,worldX,worldY,area,localX,localY);
  }

  public Coordinate offsetCoordinateBySquares(Coordinate coordinate, int offX, int offY) {

    final int globalX = coordinate.globalX + offX;
    final int globalY = coordinate.globalY + offY;

    return makeCoordinate(globalX, globalY);

  }

  public Coordinate offsetCoordinateByAreas(Coordinate coordinate, int offX, int offY) {

    final int globalX = coordinate.globalX + offX * areaSizeInSquares.getWidth();
    final int globalY = coordinate.globalY + offY * areaSizeInSquares.getHeight();

    return makeCoordinate(globalX, globalY);

  }

  public Coordinate makeRandomCoordinate() {
    return makeCoordinate(Game.RANDOM.nextInt(globalSizeInSquares.getWidth()),
                          Game.RANDOM.nextInt(globalSizeInSquares.getHeight()));
  }


  public Dimension getAreaSizeInSquares() {
    return areaSizeInSquares;
  }

}
