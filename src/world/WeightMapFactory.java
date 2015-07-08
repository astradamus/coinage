package world;

import game.Direction;
import game.Game;
import utils.Dimension;
import utils.IntegerRange;

import java.awt.Point;

/**
 * Exposes methods that produce WeightMaps via different algorithms. Each method takes a dimension
 * for the map, and a "weights by index" integer array. For each integer value in this array, that
 * integer's index in the array has a frequency in the weight map that is proportional to that
 * integer's value relative to the total of all the values in the array.
 * <p>
 * Put another way, given a weights by index of {5,15,30} with a dimension of 10x10, your given map
 * would contain 100 total values. Ten of these values would be {@code 0}, thirty of them would be
 * {@code 1}, and sixty of them would be {@code 2}.
 */
final class WeightMapFactory {

  private static final double patchesDistributionStrictness = 1.00;

  private static final double crawlerDistributionStrictness = 1.00;
  private static final IntegerRange crawlerTurningRadius = new IntegerRange(0, 7);
  private static final IntegerRange crawlerAverageChunkCountRange = new IntegerRange(1, 10);


  /**
   * This method uses a 'random patch' algorithm to generate the map. It walks through each weight
   * index that hasn't yet met its goal, placing randomly upon the map a patch of variable size and
   * "patchiness." This process is repeated over and over until the proper distribution has been
   * achieved.
   *
   * @param patchMaxRadius  The maximum distance a patch can extend from its center point.
   * @param patchPatchiness The chance each square in a patch will not be applied.
   */
  static WeightMap generateWithPatches(Dimension dimension, int[] featureWeightsByIndex,
      int patchMaxRadius, double patchPatchiness) {

    final WeightMapBundle bundle =
        new WeightMapBundle(dimension, featureWeightsByIndex, patchesDistributionStrictness);
    final int[] distancesFromGoals = bundle.distancesFromGoals;

    int featureIndex = 0;
    boolean goalUnmet = true;

    while (goalUnmet) {

      // if this feature's goal has not been met, place a patch of it.
      if (distancesFromGoals[featureIndex] > 0) {
        placePatch(bundle, dimension, featureIndex, patchMaxRadius, patchPatchiness);
      }
      else {

        // If we hit a met goal, check if all goals are met.
        goalUnmet = false;
        for (int distanceFromGoal : distancesFromGoals) {
          if (distanceFromGoal > 0) {
            goalUnmet = true;
            break;
          }
        }
      }

      // if goal is still unmet, prepare for next loop cycle by advancing index
      if (goalUnmet) {
        featureIndex++;
        featureIndex %= distancesFromGoals.length;
      }
    }

    return bundle.construct();
  }


  private static void placePatch(WeightMapBundle bundle, Dimension dimension, int placingIndex,
      int patchMaxRadius, double patchPatchiness) {

    final int patchRadius = Game.RANDOM.nextInt(patchMaxRadius);
    final int x = Game.RANDOM.nextInt(dimension.getWidth());
    final int y = Game.RANDOM.nextInt(dimension.getHeight());

    // loop through all candidate terrain slots within the patch
    for (int adjY = y - patchRadius; adjY < y + patchRadius; adjY++) {
      for (int adjX = x - patchRadius; adjX < x + patchRadius; adjX++) {

        // don't go outside map boundaries
        if (adjX < 0 || adjX >= bundle.baseMap[0].length || adjY < 0
            || adjY >= bundle.baseMap.length) {
          continue;
        }

        // check if this spot represents a hole in the patch
        if (Game.RANDOM.nextDouble() > patchPatchiness) {
          int replacingIndex = bundle.baseMap[adjY][adjX];
          bundle.adjustDistribution(placingIndex, 1); // Advance goal for this index.
          bundle.adjustDistribution(replacingIndex, -1); // Rewind goal for replaced index.
          bundle.baseMap[adjY][adjX] = placingIndex; // Place the index.
        }
      }
    }
  }


  /**
   * This method uses a 'random walk' algorithm to generate the map. For each feature, it performs a
   * series of random walks across the map, drawing that feature in its wake until its goal is
   * reached, then moving to the next feature until all are done. It will not overwrite a feature it
   * has already added, and will jump to a random new position if it has too many of these
   * collisions in a row or it walks off the edge of the map.
   */
  static WeightMap generateWithCrawler(Dimension dimension, int[] featureWeightsByIndex) {

    final WeightMapBundle bundle =
        new WeightMapBundle(dimension, featureWeightsByIndex, crawlerDistributionStrictness);

    final int[][] baseMapUnderlyingArray = bundle.baseMap;
    final int[] distancesFromGoals = bundle.distancesFromGoals;

    final int width = dimension.getWidth();
    final int height = dimension.getHeight();

    // The base map comes filled with the heaviest feature, which crawler will need.
    final int heaviestFeatureIndex = baseMapUnderlyingArray[0][0];

    // For each feature, draw random walks across the map until its goal is met.
    for (int featureIndex = 0; featureIndex < featureWeightsByIndex.length; featureIndex++) {

      if (featureIndex == heaviestFeatureIndex) {
        continue; // Skip the main feature since the map was filled with this already.
      }

      // The maximum number of times we can walk onto a feature that has already been changed
      // before we must start a new walk at a random location. Small numbers produce several small
      // clumps. Large numbers produce few large clumps. This number should be less than the
      // maximum quantity for the feature being placed.
      // The divisor determines the average number of clumps. 4 to 8 are good values for areas.
      final int collisionDivisor = crawlerAverageChunkCountRange.getRandomWithin(Game.RANDOM);
      final int maxFeatureCollisions = distancesFromGoals[featureIndex] / collisionDivisor;

      // Keep track of feature collisions.
      int featureCollisions = 0;

      // Pick an initial location and direction to start placing this feature.
      final Point position = new Point(Game.RANDOM.nextInt(width), Game.RANDOM.nextInt(height));
      Direction direction = Direction.getRandom();

      // Place this feature until we reach its goal.
      while (distancesFromGoals[featureIndex] > 0) {

        // Turn to a random direction within range.
        direction = direction.turn(crawlerTurningRadius.getRandomWithin(Game.RANDOM));

        // Move in that direction.
        position.translate(direction.relativeX, direction.relativeY);

        // If we exceed the map bounds, we must start a new walk.
        if (!dimension.getCoordinateIsWithinBounds(position)) {
          featureCollisions = maxFeatureCollisions; // Hit edge of area, must start new walk.
        }

        // Otherwise, if we hit a feature that isn't the base type, increment the collision count.
        else if (baseMapUnderlyingArray[position.y][position.x] != heaviestFeatureIndex) {
          featureCollisions++;
        }

        // Otherwise, we're free to place the feature here.
        else {
          bundle.adjustDistribution(featureIndex, 1); // Advance goal for this index.
          bundle.adjustDistribution(heaviestFeatureIndex, -1); // Rewind goal for replaced index.
          baseMapUnderlyingArray[position.y][position.x] = featureIndex; // Place the feature.
        }

        // If we've had too many collisions, move to a new location for the next loop.
        if (featureCollisions == maxFeatureCollisions) {
          position.setLocation(Game.RANDOM.nextInt(width), Game.RANDOM.nextInt(height));
          featureCollisions = 0;  // Reset to zero since we moved
        }
      }
    }

    return bundle.construct();
  }
}