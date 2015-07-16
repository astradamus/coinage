package world.blueprint;

/**
 *
 */
public interface BlueprintFeature {

  /**
   * Defines this map feature's frequency relative to the other features of its map.
   */
  int getWeight();

  /**
   * Defines the style that should be used when drawing this feature on a blueprint using the
   * crawler method.
   */
  default CrawlerStyle getCrawlerStyle() { return null; }
}