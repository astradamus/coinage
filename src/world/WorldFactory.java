package world;

import utils.Dimension;

/**
 *
 */
public class WorldFactory {

  public static final int    STDGEN_PATCH_RADIUS_LIMIT = 4;
  public static final double STDGEN_PATCH_PATCHINESS = 0.000; // % of patch candidates are discarded

  public static World standardGeneration(Dimension areaSizeInSquares, Dimension worldSizeInAreas) {

    // Get a WeightMap

    WeightMap biomeWeightMap = WeightMapFactory.generateWeightMap(STDGEN_PATCH_RADIUS_LIMIT,
        STDGEN_PATCH_PATCHINESS, Biome.getAllWeights(), worldSizeInAreas.getWidth(),
        worldSizeInAreas.getHeight());


    // Produce areas from WeightMap.

    Area[][] areas = new Area[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
    for(int y = 0; y < worldSizeInAreas.getHeight(); y++) {
      for(int x = 0; x < worldSizeInAreas.getWidth(); x++) {
        areas[y][x] = AreaFactory.standardGeneration(
            Biome.values()[biomeWeightMap.weightMap[y][x]],areaSizeInSquares);
      }
    }

    return new World(areas,areaSizeInSquares);
  }

}