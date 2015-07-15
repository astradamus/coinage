package world.blueprinter;

import game.Direction;
import game.Game;
import utils.Dimension;
import utils.IntegerRange;

import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Exposes methods that produce Blueprints via different algorithms. Each method takes a dimension
 * for the map and a feature set. Each feature in this set has a frequency in the blueprint that is
 * proportional to that feature's weight relative to the total of all the weights of all the
 * features in the set.
 */
public final class BlueprintFactory {

  private static final double patchesStrictness = 1.00;
  private static final double crawlerStrictness = 1.00;
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
  public static <E extends BlueprintFeature> Blueprint<E> generateWithPatches(Dimension dimension,
      Set<E> featureSet, int patchMaxRadius, double patchPatchiness) {

    final Blueprint<E> blueprint = new Blueprint<>(dimension, featureSet, patchesStrictness);

    final List<E> featureList = featureSet.stream().collect(Collectors.toList());
    int featureIndex = 0;
    boolean goalsUnsatisfied = true;

    while (goalsUnsatisfied) {

      final E feature = featureList.get(featureIndex);

      // If this feature's goal has not been met, place a patch of it.
      if (!blueprint.goalIsSatisfied(feature)) {
        placePatch(blueprint, dimension, feature, patchMaxRadius, patchPatchiness);
      }

      // If we hit a met goal, check if all goals are met.
      else {
        goalsUnsatisfied = !blueprint.allGoalsSatisfied();
      }

      // If goal is still unmet, prepare for next loop cycle by advancing index.
      if (goalsUnsatisfied) {
        featureIndex = (featureIndex + 1) % featureList.size();
      }
    }

    return blueprint;
  }


  private static <T extends BlueprintFeature> void placePatch(Blueprint<T> bundle,
      Dimension dimension, T placingFeature, int patchMaxRadius, double patchPatchiness) {

    final int patchRadius = Game.RANDOM.nextInt(patchMaxRadius);
    final int x = Game.RANDOM.nextInt(dimension.getWidth());
    final int y = Game.RANDOM.nextInt(dimension.getHeight());

    // loop through all candidate terrain slots within the patch
    for (int adjY = y - patchRadius; adjY < y + patchRadius; adjY++) {
      for (int adjX = x - patchRadius; adjX < x + patchRadius; adjX++) {

        // don't go outside map boundaries
        if (!dimension.getCoordinateIsWithinBounds(adjX, adjY)) {
          continue;
        }

        // check if this spot represents a hole in the patch
        if (Game.RANDOM.nextDouble() > patchPatchiness) {
          bundle.putFeature(placingFeature, adjX, adjY);
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
  public static <T extends BlueprintFeature> Blueprint<T> generateWithCrawler(Dimension dimension,
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