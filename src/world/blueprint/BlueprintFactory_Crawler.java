package world.blueprint;

import game.Direction;
import game.Game;
import utils.Dimension;
import utils.IntegerRange;

import java.awt.Point;
import java.util.List;
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


  public static <T extends BlueprintFeature> Blueprint<T> generate(Dimension dimension,
      Set<T> featureSet) {

    final Blueprint<T> blueprint = new Blueprint<>(dimension, featureSet, crawlerStrictness);

    final int width = dimension.getWidth();
    final int height = dimension.getHeight();

    // For each feature, draw random walks across the map until its goal is met.
    for (T feature : featureSet) {

      final CrawlerStyle pattern = feature.getCrawlerStyle();
      final List<CrawlerStyle.CrawlerMove> patternMoves = pattern.getCrawlerMoves();
      final int patternRepeats = pattern.getCrawlerPatternRepeats();  // Number of times to repeat this pattern
      int patternCount = 0; // Number of times this pattern has been drawn.

      if (feature == blueprint.getMostCommonFeature()) {
        continue; // Skip the most common feature since the map was filled with this already.
      }

      // The maximum number of times we can walk onto a feature that has already been changed
      // before we must start a new walk at a random location. Small numbers produce several small
      // clumps. Large numbers produce few large clumps. This number should be less than the
      // maximum quantity for the feature being placed.
      // The divisor determines the average number of clumps. 4 to 8 are good values for areas.
      // final int averageClumpCount = crawlerAverageClumpCountRange.getRandomWithin(Game.RANDOM);
      // final int collisionLimit = blueprint.getFeatureCountGoals().get(feature) / averageClumpCount;

      int moveIndex = 0; // Keeps track of the current move
      int currentRepeats = patternMoves.get(0).getRepetitions(); // Start with this pattern's first move's repeat count.
      int repeatCounter = 0;

      // Keep track of feature collisions.
      // int featureCollisions = 0;

      // Pick an initial location and direction to start placing this feature.
      final Point position = new Point(Game.RANDOM.nextInt(width), Game.RANDOM.nextInt(height));
      Direction direction = Direction.getRandom();

      // Place this feature until we reach its goal.
      while (!blueprint.goalIsSatisfied(feature)) {

        // get the type of move and do it.
        direction = direction.turn(patternMoves.get(moveIndex).getType());
        repeatCounter++;

        // Are we done with this move?
        if (repeatCounter == currentRepeats) {
          moveIndex++;   // increment the move index.
          if (moveIndex == patternMoves.size()) {
            moveIndex = 0;  // reset the move index, increment the pattern count.
            patternCount++;
          }
          currentRepeats = patternMoves.get(moveIndex).getRepetitions();  // Get the repeat for the next move.
          repeatCounter = 0; // Reset the counter.
        }

        // Move in that direction.
        position.translate(direction.relativeX, direction.relativeY);
        // Wrap if it goes off an edge. This is done to keep patterns intact.
        // Partial patterns are undesireable.
        position.x = (position.x + width) % width;
        position.y = (position.y + height) % height;

/*       // If we exceed the map bounds or the collision limit, we must start a new walk.
*        // This code will probably be obsoleted.
*        if (!dimension.getCoordinateIsWithinBounds(position)
*            || featureCollisions == collisionLimit) {
*          position.setLocation(Game.RANDOM.nextInt(width), Game.RANDOM.nextInt(height));
*          featureCollisions = 0;  // Reset to zero since we moved
*        }
*/
        // Otherwise, if we hit a feature that isn't the most common type (i.e. a tile that has
        // already been crawled over), increment the collision count.
        // If the above code is obsoleted this will need to be rearranged.
        if (blueprint.get(position.x, position.y) != blueprint.getMostCommonFeature()) {
//          featureCollisions++;
        }

        // Otherwise, we're free to place the feature here.
        else {
          blueprint.putFeature(feature, position.x, position.y);
        }

        // Have we reached the number of pattern repeats?
        // This can be changed to accommodate multiple patterns with different repeats.
        // Currently just resets the counter and moves to a new location
        // and starts drawing the current pattern again
        if (patternCount == patternRepeats) {
          position.setLocation(Game.RANDOM.nextInt(width), Game.RANDOM.nextInt(height));
          patternCount = 0;
        }
      }
    }

    return blueprint;
  }
}
