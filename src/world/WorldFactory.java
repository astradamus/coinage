package world;

/**
 *
 */
public class WorldFactory {

  public static final int    STDGEN_PATCH_RADIUS_LIMIT = 4;
  public static final double STDGEN_PATCH_PATCHINESS = 0.000; // % of patch candidates are discarded

  public static World standardGeneration(int worldWidthInAreas, int worldHeightInAreas,
                                         int areaWidth, int areaHeight) {

    // Get a WeightMap

    WeightMap biomeWeightMap = WeightMapFactory.generateWeightMap(STDGEN_PATCH_RADIUS_LIMIT,
        STDGEN_PATCH_PATCHINESS, Biome.getAllWeights(), worldWidthInAreas, worldHeightInAreas);


    // Produce areas from WeightMap.

    Area[][] areas = new Area[worldHeightInAreas][worldWidthInAreas];
    for(int y = 0; y < worldHeightInAreas; y++) {
      for(int x = 0; x < worldWidthInAreas; x++) {
        areas[y][x] = AreaFactory.standardGeneration(
            Biome.values()[biomeWeightMap.weightMap[y][x]], areaWidth, areaHeight);
      }
    }

    return new World(areas);
  }

}