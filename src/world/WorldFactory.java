package world;

import game.Game;

/**
 *
 */
public class WorldFactory {

  public static final int    STDGEN_PATCH_RADIUS_LIMIT = 2;
  public static final double STDGEN_PATCH_HOLINESS = 0.000; // % of patch candidates are discarded
  public static final double STDGEN_BIOME_RATE_STRICTNESS = 0.95; //

  public static World standardGeneration(int worldWidthInAreas, int worldHeightInAreas,
                                         int areaWidth, int areaHeight) {

    // FIRST, GENERATE BIOMES

    int[][] biomeBluePrint = new int[worldHeightInAreas][worldWidthInAreas];

    // determine how many squares in the world
    int squares = worldWidthInAreas*worldHeightInAreas;

    // get total of biome weights and use them to divide individual weights for % rate.
    //  Also get the highest weight (to use as base biome).
    int weightTotal = 0;
    int heaviestIndex = -1;
    for (int i = 0; i < Biome.values().length; i++) {
      weightTotal += Biome.values()[i].biomeWeight;
      if (heaviestIndex == -1 || Biome.values()[i].biomeWeight > Biome.values()[heaviestIndex].biomeWeight) {
        heaviestIndex = i;
      }
    }

    // determine what the distribution of squares should be, adjusted for strictness
    int[] distribution = new int[Biome.values().length];
    int[] distributionGoals = new int[Biome.values().length];
    for (int i = 0; i < Biome.values().length; i++) {
      // weights
      distribution[i] =
          (int) (squares * (Biome.values()[i].biomeWeight/(double)weightTotal)
              * STDGEN_BIOME_RATE_STRICTNESS);
      distributionGoals[i] = distribution[i];
    }


    // Terrain Generation: Set every square on the map to the base TerrainType index, then cycle
    //   through each TerrainType that has not met its goal, placing patches of that type on the
    //   map at random, until each goal has been met. Note that strictness rates too high may
    //   cause very long/near-infinite loops.


    // set the base Biome index
    for(int y = 0; y < worldHeightInAreas; y++) {
      for(int x = 0; x < worldWidthInAreas; x++) {
        biomeBluePrint[y][x] = heaviestIndex;
        distributionGoals[heaviestIndex]--;
      }
    }

    int loopIndex = 0;
    boolean goalUnmet = true;
    while (goalUnmet) {

      // if this TerrainType's goal has not been met, place a patch of it
      if (distributionGoals[loopIndex] > 0) {

        placePatch(distributionGoals,biomeBluePrint,loopIndex,
            Game.RANDOM.nextInt(worldWidthInAreas),Game.RANDOM.nextInt(worldHeightInAreas));

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

    // FINALLY, PRODUCE AREAS FROM BLUEPRINT AND RETURN NEW WORLD
    Area[][] areas = new Area[worldHeightInAreas][worldWidthInAreas];
    for(int y = 0; y < worldHeightInAreas; y++) {
      for(int x = 0; x < worldWidthInAreas; x++) {
        areas[y][x] = AreaFactory.standardGeneration(Biome.values()[biomeBluePrint[y][x]],areaWidth,
            areaHeight);
      }
    }

    return new World(areas);
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
