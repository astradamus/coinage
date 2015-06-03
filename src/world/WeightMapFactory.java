package world;

import game.Game;

/**
 *
 */
public final class WeightMapFactory {

  public static final double DISTRIBUTION_STRICTNESS = 0.95; //

  /**
   * <p>Produces an int[][] of size height*width, and populates it with values within range
   * [0-weights.length).</p>
   * <p>The distribution of this population is determined by the value in weights
   * for the given index. The weight value for an index is divided by the total weight value of
   * all indexes, and this ratio is multiplied by the total number of squares in the int[][].</p>
   *
   * @param patchMaxRadius The maximum distance a patch can extend from its center point.
   * @param patchPatchiness The chance each square in a patch will not be applied.
   * @return A WeightMap containing the int[][] and an int[] containing the *target*
   * (NOT ACTUAL) amount of each index on the map.
   */
  static WeightMap generateWeightMap(int patchMaxRadius, double patchPatchiness,
                                          int[] weights, int width, int height) {

    int[][] blueprint = new int[height][width];

    // determine number of squares in the area
    int squares = width*height;

    // get total of weights (for divisor) and the highest weight (to use as base).
    int weightTotal = 0;
    int heaviestIndex = -1;
    for (int i = 0; i < weights.length; i++) {
      weightTotal += weights[i];
      if (heaviestIndex == -1 || weights[i] > weights[heaviestIndex]) {
        heaviestIndex = i;
      }
    }

    // determine what the distribution of squares should be, adjusted for strictness
    int[] distribution = new int[weights.length];
    int[] distributionGoals = new int[weights.length];
    for (int i = 0; i < weights.length; i++) {
      // weights
      distribution[i] =
          (int) (squares * (weights[i]/(double)weightTotal)
              * DISTRIBUTION_STRICTNESS);
      distributionGoals[i] = distribution[i];
    }


    // Generation: Set every square on the map to the base index, then cycle through each index
    //   that has not met its goal, placing patches of that index on the map at random, until
    //   each goal has been met. Note that strictness rates too high may cause very long/infinite
    //   loops.

    // set the base index
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        blueprint[y][x] = heaviestIndex;
        distributionGoals[heaviestIndex]--;
      }
    }

    int loopIndex = 0;
    boolean goalUnmet = true;
    while (goalUnmet) {

      // if this TerrainType's goal has not been met, place a patch of it
      if (distributionGoals[loopIndex] > 0) {

        placePatch(patchMaxRadius, patchPatchiness, distributionGoals, blueprint, loopIndex,
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
    return new WeightMap(blueprint,distribution);
  }

  private static void placePatch(int patchMaxRadius, double patchPatchiness,
                                 int[] distributionGoals, int[][] targetTerrain,
                                 int placingIndex, int x, int y) {

    // decide how big this patch will be
    int patchRadius = Game.RANDOM.nextInt(patchMaxRadius);

    // loop through all candidate terrain slots within the patch
    for(int adjY = y-patchRadius; adjY < y+patchRadius; adjY++) {
      for(int adjX = x-patchRadius; adjX < x+patchRadius; adjX++) {

        // don't go outside map boundaries
        if (adjX < 0 || adjX >= targetTerrain[0].length
            || adjY < 0 || adjY >= targetTerrain.length) {
          continue;
        }

        // check if this spot represents a hole in the patch
        if (Game.RANDOM.nextDouble() > patchPatchiness) {
          distributionGoals[targetTerrain[adjY][adjX]]++; // increment goal for replaced type
          distributionGoals[placingIndex]--; // decrement goal for placed type
          targetTerrain[adjY][adjX] = placingIndex; // place the type
        }

      }
    }
  }

}
