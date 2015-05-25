package world;

/**
 *
 */
public final class WeightMap {
  public final int[][] weightMap;
  public final int[] distribution;

  public WeightMap(int[][] weightMap, int[] distribution) {
    this.weightMap = weightMap;
    this.distribution = distribution;
  }
}
