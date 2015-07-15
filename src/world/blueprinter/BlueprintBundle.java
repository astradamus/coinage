package world.blueprinter;

import utils.Dimension;

/**
 *
 */
public class BlueprintBundle {

  final int[][] baseMap;
  final int[] actualDistribution;
  final int[] distancesFromGoals;

  public BlueprintBundle(Dimension dimension, int[] weightsByIndex, double distributionStrictness) {

    // determine number of squares in the area
    final int squares = dimension.getArea();

    // get total of all weights (for divisor) and the highest weight (to use as base).
    int weightTotal = 0;
    int heaviestFeatureIndex = -1;
    for (int i = 0; i < weightsByIndex.length; i++) {
      weightTotal += weightsByIndex[i];
      if (heaviestFeatureIndex == -1 || weightsByIndex[i] > weightsByIndex[heaviestFeatureIndex]) {
        heaviestFeatureIndex = i;
      }
    }

    // Determine what the distribution of feature indices should be, adjusted for strictness.
    int[] distancesFromGoals = new int[weightsByIndex.length];
    for (int i = 0; i < weightsByIndex.length; i++) {
      distancesFromGoals[i] =
          (int) (squares * (weightsByIndex[i] / (double) weightTotal) * distributionStrictness);
    }

    // Make array for tracking actual distribution.
    final int[] actualDistribution = new int[weightsByIndex.length];

    // Make a base map filled with the heaviest feature index.
    final int[][] baseMap = new int[dimension.getHeight()][dimension.getWidth()];

    for (int y = 0; y < dimension.getHeight(); y++) {
      for (int x = 0; x < dimension.getWidth(); x++) {
        baseMap[y][x] = heaviestFeatureIndex;
        distancesFromGoals[heaviestFeatureIndex]--;
        actualDistribution[heaviestFeatureIndex]++;
      }
    }

    this.baseMap = baseMap;
    this.actualDistribution = actualDistribution;
    this.distancesFromGoals = distancesFromGoals;
  }

  public void adjustDistribution(int index, int value) {
    actualDistribution[index] += value;
    distancesFromGoals[index] -= value;
  }

  public Blueprint construct() {
    return new Blueprint(baseMap,actualDistribution);
  }

}