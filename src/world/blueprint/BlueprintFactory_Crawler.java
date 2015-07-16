package world.blueprint;

import game.Direction;
import game.Game;
import utils.Dimension;
import utils.IntegerRange;

import java.awt.Point;
import java.util.Set;

/**
 * This factory uses a 'random walk' algorithm to generate the map. For each feature, it performs a
 * series of random walks across the map, drawing that feature in its wake until its goal is
 * reached, then moving to the next feature until all are done. It will not overwrite a feature it
 * has already added, and will jump to a random new position if it has too many of these collisions
 * in a row or it walks off the edge of the map.
 * <p>
 * Each feature in the provided set has a frequency in the blueprint that is proportional to that
 * feature's weight relative to the total of all the weights of all the features in the set.
 */
public class BlueprintFactory_Crawler {

  private static final double crawlerStrictness = 1.00;
  private static final IntegerRange crawlerTurningRadius = new IntegerRange(0, 7);
  private static final IntegerRange crawlerAverageChunkCountRange = new IntegerRange(1, 10);


  public static <T extends BlueprintFeature> Blueprint<T> generate(Dimension dimension,
      Set<T> featureSet) {

    final Blueprint<T> blueprint = new Blueprint<>(dimension, featureSet, crawlerStrictness);

    final int width = dimension.getWidth();
    final int height = dimension.getHeight();

    // For each feature, draw random walks across the map until its goal is met.
    for (T feature : featureSet) {

      if (feature == blueprint.getMostCommonFeature()) {
        continue; // Skip the most common feature since the map was filled with this already.
      }

      // The maximum number of times we can walk onto a feature that has already been changed
      // before we must start a new walk at a random location. Small numbers produce several small
      // clumps. Large numbers produce few large clumps. This number should be less than the
      // maximum quantity for the feature being placed.
      // The divisor determines the average number of clumps. 4 to 8 are good values for areas.
      final int averageChunkCount = crawlerAverageChunkCountRange.getRandomWithin(Game.RANDOM);
      final int collisionLimit = blueprint.getFeatureCountGoals().get(feature) / averageChunkCount;

      // Keep track of feature collisions.
      int featureCollisions = 0;

      // Pick an initial location and direction to start placing this feature.
      final Point position = new Point(Game.RANDOM.nextInt(width), Game.RANDOM.nextInt(height));
      Direction direction = Direction.getRandom();

      // Place this feature until we reach its goal.
      while (!blueprint.goalIsSatisfied(feature)) {

        // Turn to a random direction within range.
        direction = direction.turn(crawlerTurningRadius.getRandomWithin(Game.RANDOM));

        // Move in that direction.
        position.translate(direction.relativeX, direction.relativeY);

        // If we exceed the map bounds or the collision limit, we must start a new walk.
        if (!dimension.getCoordinateIsWithinBounds(position)
            || featureCollisions == collisionLimit) {
          position.setLocation(Game.RANDOM.nextInt(width), Game.RANDOM.nextInt(height));
          featureCollisions = 0;  // Reset to zero since we moved
        }

        // Otherwise, if we hit a feature that isn't the most common type (i.e. a tile that has
        // already been crawled over), increment the collision count.
        else if (blueprint.get(position.x, position.y) != blueprint.getMostCommonFeature()) {
          featureCollisions++;
        }

        // Otherwise, we're free to place the feature here.
        else {
          blueprint.putFeature(feature, position.x, position.y);
        }
      }
    }

    return blueprint;
  }
}
