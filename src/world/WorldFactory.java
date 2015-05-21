package world;

/**
 *
 */
public class WorldFactory {

  public static World standardGeneration(int worldWidthInAreas, int worldHeightInAreas,
                                  int areaWidth, int areaHeight) {
    Area[][] areas = new Area[worldHeightInAreas][worldWidthInAreas];

    for(int y = 0; y < worldHeightInAreas; y++) {
      for(int x = 0; x < worldWidthInAreas; x++) {
        areas[y][x] = AreaFactory.standardGeneration(areaWidth,areaHeight);
      }
    }

    return new World(areas);

  }
}
