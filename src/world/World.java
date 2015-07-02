package world;

import game.Game;
import utils.Dimension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

  public Coordinate makeRandomCoordinate() {
    return new Coordinate(Game.RANDOM.nextInt(globalSizeInSquares.getWidth()),
                          Game.RANDOM.nextInt(globalSizeInSquares.getHeight()));
  }


  public Dimension getAreaSizeInSquares() {
    return areaSizeInSquares;
  }

  public Set<Area> getAllAreas() {
    Set<Area> all = new HashSet<>();
    for(int y = 0; y < worldSizeInAreas.getHeight(); y++) {
      all.addAll(Arrays.asList(areas[y]));
    }
    return all;
  }

  public Set<Area> getAllAreasWithinRange(Coordinate target, int radius) {
    final WorldMapCoordinate worldTarget = convertToWorldMapCoordinate(target);
    Set<Area> all = new HashSet<>();
    for(int y = worldTarget.worldAreasY - radius; y <= worldTarget.worldAreasY+radius; y++) {
      if (y < 0 || y >= worldSizeInAreas.getHeight()) {
        continue;
      }
      for(int x = worldTarget.worldAreasX - radius; x <= worldTarget.worldAreasX+radius; x++) {
        if (x < 0 || x >= worldSizeInAreas.getWidth()) {
          continue;
        }

        all.add(areas[y][x]);

      }
    }
    return all;
  }

  public WorldMapCoordinate convertToWorldMapCoordinate(Coordinate coordinate) {
    return new WorldMapCoordinate(coordinate.globalX/areaSizeInSquares.getWidth(), coordinate.globalY/areaSizeInSquares.getHeight());
  }

  public AreaCoordinate convertToAreaCoordinate(Coordinate coordinate) {
    if (coordinate == null) {
      return null;
    }
    return new AreaCoordinate(coordinate.globalX%areaSizeInSquares.getWidth(),
        coordinate.globalY%areaSizeInSquares.getHeight());
  }

  public Area getArea(Coordinate coordinate) {
    if (coordinate == null) {
      return null;
    }

    try {
      final WorldMapCoordinate worldMapCoordinate = convertToWorldMapCoordinate(coordinate);
      return areas[worldMapCoordinate.worldAreasY][worldMapCoordinate.worldAreasX];
    } catch (IndexOutOfBoundsException iob) {
      return null; // Target is not valid.
    }
  }

  public Square getSquare(Coordinate coordinate) {
    if (coordinate == null) {
      return null;
    }

    try {
      final AreaCoordinate areaCoordinate = convertToAreaCoordinate(coordinate);
      final Area area = getArea(coordinate);
      if (area==null) {return null;}
      return area.getSquare(areaCoordinate);
    } catch (IndexOutOfBoundsException iob) {
      return null; // Target is not valid.
    }
  }

  public AreaCoordinate offsetAreaCoordinate(AreaCoordinate areaCoordinate, int offX, int offY) {
    final AreaCoordinate offset = new AreaCoordinate(areaCoordinate.areaX+offX,areaCoordinate
        .areaY+offY);
    if (areaSizeInSquares.getCoordinateIsWithinBounds(offset.areaX, offset.areaY)) {
      return offset;
    }
    else {
      return null;
    }
  }

}