package world;

import game.Game;
import game.Physical;
import thing.ThingFactory;
import utils.Dimension;

/**
 *
 */
public class AreaFactory {

  public static final int    STDGEN_PATCH_RADIUS_LIMIT = 5;
  public static final double STDGEN_PATCH_PATCHINESS = 0.300; // % of patch candidates are discarded

  public static Area standardGeneration(Biome biome, Dimension areaSizeInSquares) {

    int width = areaSizeInSquares.getWidth();
    int height = areaSizeInSquares.getHeight();

    // Get a WeightMap
    WeightMap terrainWeightMap =
        WeightMapFactory.generateWeightMap(STDGEN_PATCH_RADIUS_LIMIT, STDGEN_PATCH_PATCHINESS,
            biome.terrainWeights, width, height);


    // Generate Features
    Physical[][] physicals = generateFeatures(biome, terrainWeightMap, width, height);


    // Produce Terrain from WeightMap
    Square[][] squares = new Square[height][width];
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        Terrain terrain = new Terrain(biome.terrainTypes[terrainWeightMap.weightMap[y][x]]);
        Square square = new Square(terrain);
        Physical physical = physicals[y][x];
        if (physical != null) {
          square.put(physical);
        }
        squares[y][x] = square;
      }
    }

    return new Area(biome, squares);

  }


  private static Physical[][] generateFeatures(Biome biome, WeightMap terrainWeightMap,
                                               int width, int height) {

    Physical[][] physicals = new Physical[height][width];

    for (int terrainTypeIndex = 0; terrainTypeIndex < biome.terrainTypes.length; terrainTypeIndex++) {

      for (int featureIndex = 0;
           featureIndex < biome.featureIDs[terrainTypeIndex].length;
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
          System.out.println("Failed to find spot for "+featureID+". Frequency too high?");
        }
      }
    }
    return physicals;
  }

}
