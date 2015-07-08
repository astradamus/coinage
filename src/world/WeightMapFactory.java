package world;

import game.Game;
import game.Direction;
/**
 *
 */
final class WeightMapFactory {

  private static final double DISTRIBUTION_STRICTNESS = 0.95; //

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

  static WeightMap generateWeightMapCrawler(int[] weights, int width, int height){
    // Consider renaming "weights" to something else, like "featureWeights",
    // since the index is the "feature" being placed and the content is the actual "weight"

    final int RELOCATE_METHOD = 2;  // How to draw the features on the map. 0-2 See below for descriptions.

    int[][] map = new int[height][width]; // map contains the layout of the map features.
    // determine number of squares in the area
    int squares = width*height;
    // get total of weights (for divisor) and the feature with the highest weight (to use as base).
    int weightTotal = 0;  // Sum of all the weights
    int maxWeight = -1;   // Maximum weight found
    int mainFeature = 0;   // Feature containing the maximum weight.
    for (int i = 0; i < weights.length; i++) {
      weightTotal += weights[i];
      if (weights[i] > maxWeight) {
        maxWeight = weights[i];
        mainFeature = i;
      }
    }
    int[] featureQuantity = new int[weights.length];  // Contains the amount of each feature type for this map.
    for (int i = 0; i < weights.length; i++) {
      featureQuantity[i] = (squares * weights[i]) / weightTotal;   // Since Java ints are 32 bit this can be done without the casting.
    }
    // Keeps track of the main feature quantity as other features are placed on the map.
    // I do this to ensure that the total feature quantity is accurate since integer division can lead to rounding errors that accumulate.
    // since the map starts out filled with the main feature I initialize it with squares.
    int mainFeatureCount = squares;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        map[y][x] = mainFeature;  // Fill map with the dominant feature type.
      }
    }

    // Half of each dimension. Used for determining current position quadrant.
    int halfWidth = width / 2;
    int halfHeight = height / 2;

    // Range of turn angles in increments of 45 degrees. 1 - 9 range.
    int turnRange = 7;
    // Amount to offset the turn direction.
    // turnRange = 3 and turnOffset = -1 will produce long trails.
    // turnRange = 8 and turnOffset = 0 is just the original random walk.
    // turnOffset should be in the range -7 to 7.
    int turnOffset = -1 * (turnRange / 2);

    // Loop through the feature types and fill the map with them limited only by their quantity.
    for (int featureType = 0; featureType < weights.length; featureType++) {
      if (featureType == mainFeature)   // Skip the main feature type since the map was filled with this already.
        continue;
      // The maximum number of times we encounter a feature
      // that cannot be changed before we move to another area.
      // small numbers produce several small clumps. Large numbers produce few large clumps.
      // This number should be less than the maximum quantity for the feature being placed.
      // The divisor determines the average number of clumps. This can be turned into a variable and changed based on need.
      // 4 - 8 are good values for areas. 2 might be better for the world map.
      int maxFeatureCollisions = featureQuantity[featureType] / 6;  // The maximum number of allowed collisions
      int featureCount = 0; // Number of times this feature has been placed.
      int featureCollisions = 0;  // Keeps track of feature collisions.

      // Current position to place a feature;
      // Pick a random location on the map to start placing this feature.
      int x = Game.RANDOM.nextInt(width);
      int y = Game.RANDOM.nextInt(height);
      // Pick a starting direction to move.
      Direction direction = Direction.getRandom();

      // Place feature until we reach the quantity required for this map.
      while (featureCount < featureQuantity[featureType]) {
        // If we get too many collisions move to a new location and continue.
        if (featureCollisions == maxFeatureCollisions) {
          featureCollisions = 0;  // Reset to zero since we moved
          switch (RELOCATE_METHOD) {
            // 0 - Do nothing
            case 0: break;
            // 1 - Pick a new random location
            case 1: x = Game.RANDOM.nextInt(width);
              y = Game.RANDOM.nextInt(height);
              break;
            // 2 - Move to the opposite quadrant in the same relative position with a small random value added.
            case 2: x = (x + halfWidth + Game.RANDOM.nextInt(halfWidth / 2)) % width;
              y = (y + halfHeight + Game.RANDOM.nextInt(halfHeight / 2)) % height;
          }
        }
        // Pick a direction to turn. See above for tips.
        int turnAmount = Game.RANDOM.nextInt(turnRange) + turnOffset;
        // Turn in that direction
        direction = direction.turn(turnAmount);
        // Move in a random direction and wrap around if it exceeds the map size.
        // By adding the dimension, it wraps around if it goes less than zero.
        x = (x + direction.relativeX + width) % width;
        y = (y + direction.relativeY + height) % height;

        // Place this feature type only if the current location contains the main feature type.
        // This avoids have to keep going back over the map reapplying features if their count goes
        // below their quantity. If this generates unnatural looking maps it can be changed.
        if (map[y][x] != mainFeature) {
          featureCollisions++;
          continue;
        }
        // Yay! We can place a feature.
        map[y][x] = featureType;
        featureCount++;   // Increment the count.
        mainFeatureCount--; // Decrement the main feature count.
      }
    }
    featureQuantity[mainFeature] = mainFeatureCount;  // Store the corrected value for the main feature.
    return new WeightMap(map, featureQuantity);
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
