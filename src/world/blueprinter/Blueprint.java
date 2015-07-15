package world.blueprinter;

/**
 *
 */
public final class Blueprint {
  public final int[][] weightMap;
  public final int[] distribution;

  public Blueprint(int[][] weightMap, int[] distribution) {
    this.weightMap = weightMap;
    this.distribution = distribution;
  }
}
