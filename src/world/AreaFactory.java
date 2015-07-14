package world;

import game.Game;
import game.io.GameResources;
import game.physical.Physical;
import thing.ThingFactory;
import utils.Dimension;

/**
 *
 */
class AreaFactory {

  private static final int STDGEN_PATCH_RADIUS_LIMIT = 5;
  private static final double STDGEN_PATCH_PATCHINESS = 0.300; // % of patch candidates to discard.


  public static Area standardGeneration(Biome biome, Dimension areaSizeInSquares) {

    final int width = areaSizeInSquares.getWidth();
    final int height = areaSizeInSquares.getHeight();

    // Get a WeightMap
    final WeightMap terrainWeightMap =
        WeightMapFactory.generateWithCrawler(areaSizeInSquares, biome.terrainWeights);

    // Generate Features
    final Physical[][] physicals = generateFeatures(biome, terrainWeightMap, width, height);

    // Produce square map from WeightMap
    final Square[][] squares = new Square[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {

        final String terrainTypeID = biome.terrainTypeIDs[terrainWeightMap.weightMap[y][x]];
        final Terrain terrain = new Terrain(GameResources.getTerrainTypeByID(terrainTypeID));
        final Square square = new Square(terrain);
        final Physical physical = physicals[y][x];

        if (physical != null) {
          square.put(physical);
        }

        squares[y][x] = square;
      }
    }

    return new Area(biome, squares);
  }


  private static Physical[][] generateFeatures(Biome biome, WeightMap terrainWeightMap, int width,
      int height) {

    Physical[][] physicals = new Physical[height][width];

    for (int terrainTypeIndex = 0; terrainTypeIndex < biome.terrainTypeIDs.length;
        terrainTypeIndex++) {

      for (int featureIndex = 0; featureIndex < biome.featureIDs[terrainTypeIndex].length;
          featureIndex++) {

        String featureID = biome.featureIDs[terrainTypeIndex][featureIndex];

        int featureCount = (int) (terrainWeightMap.distribution[terrainTypeIndex]
            * biome.featureFrequencies[terrainTypeIndex][featureIndex]);

        // pick random points until we've picked a matching terrain that has no physicals, then
        //   place the feature and continue the search until we've placed all the features or
        //   searched for too long
        int searchLimit = 10000;

        while (featureCount > 0 && searchLimit > 0) {
          searchLimit--;

          int x = Game.RANDOM.nextInt(width);
          int y = Game.RANDOM.nextInt(height);

          if (terrainWeightMap.weightMap[y][x] == terrainTypeIndex && physicals[y][x] == null) {
            physicals[y][x] = ThingFactory.makeThing(featureID);
            featureCount--;
            searchLimit = 10000;
          }
        }
        if (searchLimit == 0) {
          System.out.println("Failed to find spot for " + featureID + ". Frequency too high?");
        }
      }
    }
    return physicals;
  }
}
