package world.blueprinter;

import utils.Array2D;

/**
 *
 */
public final class Blueprint {
  public final Array2D<Integer> weightMap;
  public final int[] distribution;

  public Blueprint(Array2D<Integer> weightMap, int[] distribution) {
    this.weightMap = weightMap;
    this.distribution = distribution;
  }
}
