package world.blueprint;

import game.Game;
import utils.Dimension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This method uses a 'random patch' algorithm to generate the map. It walks through each weight
 * index that hasn't yet met its goal, placing randomly upon the map a patch of variable size and
 * "patchiness." This process is repeated over and over until the proper distribution has been
 * achieved.
 * <p>
 * Each feature in the provided set has a frequency in the blueprint that is proportional to that
 * feature's weight relative to the total of all the weights of all the features in the set.
 */
public final class BlueprintFactory_Patches {

  private static final double patchesStrictness = 1.00;


  /**
   * @param patchMaxRadius  The maximum distance a patch can extend from its center point.
   * @param patchPatchiness The chance each square in a patch will not be applied.
   */
  public static <E extends BlueprintFeature> Blueprint<E> generate(Dimension dimension,
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
}