package world;

import game.Game;
import game.Physical;
import thing.ThingFactory;

/**
 *
 */
public class AreaFactory {

  public static final int    STDGEN_PATCH_RADIUS_LIMIT = 5;
  public static final double STDGEN_PATCH_HOLINESS = 0.300; // % of patch candidates are discarded
  public static final double STDGEN_TERRAIN_RATE_STRICTNESS = 0.95; //

  public static Area standardGeneration(Biome biome, int width, int height) {

    // FIRST, GENERATE TERRAIN

    int[][] terrainBluePrint = new int[height][width];

    // determine how many squares in the area
    int squares = width*height;

    // get total of terrainType weights and use them to divide individual weights for % rate.
    //  Also get the highest weight (to use as base terrain).
    int weightTotal = 0;
    int heaviestIndex = -1;
    for (int i = 0; i < biome.terrainTypes.length; i++) {
      weightTotal += biome.terrainWeights[i];
      if (heaviestIndex == -1 || biome.terrainWeights[i] > biome.terrainWeights[heaviestIndex]) {
        heaviestIndex = i;
      }
    }

    // determine what the distribution of squares should be, adjusted for strictness
    int[] distribution = new int[biome.terrainTypes.length];
    int[] distributionGoals = new int[biome.terrainTypes.length];
    for (int i = 0; i < biome.terrainTypes.length; i++) {
      // weights
      distribution[i] =
          (int) (squares * (biome.terrainWeights[i]/(double)weightTotal)
              * STDGEN_TERRAIN_RATE_STRICTNESS);
      distributionGoals[i] = distribution[i];
    }


    // Terrain Generation: Set every square on the map to the base TerrainType index, then cycle
    //   through each TerrainType that has not met its goal, placing patches of that type on the
    //   map at random, until each goal has been met. Note that strictness rates too high may
    //   cause very long/infinite loops.


    // set the base TerrainType index
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        terrainBluePrint[y][x] = heaviestIndex;
        distributionGoals[heaviestIndex]--;
      }
    }

    int loopIndex = 0;
    boolean goalUnmet = true;
    while (goalUnmet) {

      // if this TerrainType's goal has not been met, place a patch of it
      if (distributionGoals[loopIndex] > 0) {

        placePatch(distributionGoals,terrainBluePrint,loopIndex,
            Game.RANDOM.nextInt(width),Game.RANDOM.nextInt(height));

      } else {

        // if we hit a met goal, check if all goals are met
        goalUnmet = false;
        for (int distribution_goal : distributionGoals) {
          if (distribution_goal > 0) {
            goalUnmet = true;
            break;
          }
        }
      }

      // if goal is still unmet, prepare for next loop cycle by advancing index
      if (goalUnmet) {
        loopIndex++;
        loopIndex %= distributionGoals.length;
      }
    }



    // SECOND, GENERATE FEATURES

    Physical[][] physicals = new Physical[height][width];

    for (int terrainTypeIndex = 0; terrainTypeIndex < biome.terrainTypes.length; terrainTypeIndex++) {

      for (int featureIndex = 0;
           featureIndex < biome.featureIDs[terrainTypeIndex].length;
           featureIndex++) {

        String featureID = biome.featureIDs[terrainTypeIndex][featureIndex];

        int featureCount = (int) (distribution[terrainTypeIndex]
                                  * biome.featureFrequencies[terrainTypeIndex][featureIndex]);

        // pick random points until we've picked a matching terrain that has no physicals, then
        //   place the feature and continue the search until we've placed all the features or
        //   searched for too long
        int searchLimit = 10000;

        while (featureCount > 0 && searchLimit > 0) {
          searchLimit--;

          int x = Game.RANDOM.nextInt(width);
          int y = Game.RANDOM.nextInt(height);

          if (terrainBluePrint[y][x] == terrainTypeIndex && physicals[y][x] == null) {
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

    // FINALLY, PRODUCE TERRAIN FROM BLUEPRINT AND RETURN NEW AREA
    Terrain[][] terrain = new Terrain[height][width];
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        terrain[y][x] = new Terrain(biome.terrainTypes[terrainBluePrint[y][x]]);
      }
    }

    return new Area(biome, terrain, physicals);
  }

  /**
   * Generate terrain for each spot within a randomly sized range (limited by
   * STDGEN_PATCH_RADIUS_LIMIT) around the given x/y coordinate. For each spot in range, there is
   * a STDGEN_PATCH_HOLINESS chance of no terrain being generated.
   */
  private static void placePatch(int[] distributionGoals, int[][] targetTerrain,
                                 int placingTypeIndex, int x, int y) {

    // decide how big this patch will be
    int patchRadius = Game.RANDOM.nextInt(STDGEN_PATCH_RADIUS_LIMIT);

    // loop through all candidate terrain slots within the patch
    for(int adjY = y-patchRadius; adjY < y+patchRadius; adjY++) {
      for(int adjX = x-patchRadius; adjX < x+patchRadius; adjX++) {

        // don't go outside map boundaries
        if (adjX < 0 || adjX >= targetTerrain.length
            || adjY < 0 || adjY >= targetTerrain[0].length) {
          continue;
        }

        // check if this spot represents a hole in the patch
        if (Game.RANDOM.nextDouble() > STDGEN_PATCH_HOLINESS) {
          distributionGoals[targetTerrain[adjY][adjX]]++; // increment goal for replaced type
          distributionGoals[placingTypeIndex]--; // decrement goal for placed type
          targetTerrain[adjY][adjX] = placingTypeIndex; // place the type
        }

      }
    }
  }


}
