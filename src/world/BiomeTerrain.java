package world;

import world.blueprint.BlueprintFeature;
import world.blueprint.CrawlerStyle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Data class that defines a terrain type ID, an associated weight, and one or more features that
 * may appear on that terrain type. Used by a Biome to define its constituent terrain types,
 * their relative frequency, and what features they can present.
 */
public class BiomeTerrain implements BlueprintFeature {
  private final String terrainTypeID;
  private final int weight;
  private final CrawlerStyle crawlerStyle;
  private final List<BiomeProp> biomeProps;


  public BiomeTerrain(String terrainTypeID, int weight, CrawlerStyle crawlerStyle) {
    this.terrainTypeID = terrainTypeID;
    this.weight = weight;
    this.crawlerStyle = crawlerStyle;
    this.biomeProps = Collections.emptyList();
  }


  public BiomeTerrain(String terrainTypeID, int weight, CrawlerStyle crawlerStyle,
      BiomeProp... biomeProps) {
    this.terrainTypeID = terrainTypeID;
    this.weight = weight;
    this.crawlerStyle = crawlerStyle;
    this.biomeProps = Collections.unmodifiableList(Arrays.asList(biomeProps));
  }


  public String getTerrainTypeID() {
    return terrainTypeID;
  }

  @Override
  public int getWeight() {
    return weight;
  }


  public List<BiomeProp> getBiomeProps() {
    return biomeProps;
  }


  @Override
  public CrawlerStyle getCrawlerStyle() {
    return crawlerStyle;
  }
}