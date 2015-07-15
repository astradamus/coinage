package world;

import game.physical.Appearance;
import world.blueprinter.BlueprintFeature;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public enum Biome implements BlueprintFeature {

  GRASSLAND(8, new Appearance('G', new Color(7, 140, 0), new Color(8, 96, 0)),

      new BiomeTerrain("GRASS", 40,
          new BiomeProp("TREE", 0.02)),
      new BiomeTerrain("DIRT", 3,
          new BiomeProp("TREE", 0.01)),
      new BiomeTerrain("ROCK", 1,
          new BiomeProp("BOULDER", 0.002),
          new BiomeProp("STONE", 0.0001))),

  FOREST(4, new Appearance('F', new Color(28, 82, 0), new Color(3, 33, 0)),

      new BiomeTerrain("GRASS", 32,
          new BiomeProp("TREE", 0.12),
          new BiomeProp("UNDERGROWTH", 0.12)),
      new BiomeTerrain("DIRT", 2,
          new BiomeProp("TREE", 0.03)),
      new BiomeTerrain("ROCK", 1,
          new BiomeProp("BOULDER", 0.001),
          new BiomeProp("STONE", 0.0001))),

  CRAGS(2, new Appearance('C', new Color(205, 205, 205), new Color(85, 85, 85)),

      new BiomeTerrain("GRASS", 1,
          new BiomeProp("TREE", 0.004)),
      new BiomeTerrain("DIRT", 2,
          new BiomeProp("TREE", 0.002)),
      new BiomeTerrain("ROCK", 16,
          new BiomeProp("BOULDER", 0.15),
          new BiomeProp("STONE", 0.05))),

  DESERT(1, new Appearance('D', new Color(255, 204, 0), new Color(114, 90, 0)),

      new BiomeTerrain("SAND", 20,
          new BiomeProp("DUNE", 0.6),
          new BiomeProp("CACTUS", 0.005)),
      new BiomeTerrain("SANDSTONE", 1,
          new BiomeProp("BOULDER_SANDSTONE", 0.003),
          new BiomeProp("CACTUS", 0.002))),

  BADLANDS(1, new Appearance('B', new Color(144, 71, 0), new Color(76, 14, 0)),

      new BiomeTerrain("DIRT", 1),
      new BiomeTerrain("SAND", 3,
          new BiomeProp("CACTUS", 0.001)),
      new BiomeTerrain("SANDSTONE", 30,
          new BiomeProp("BOULDER_SANDSTONE", 0.05),
          new BiomeProp("CACTUS", 0.004))),

  SWAMP(1, new Appearance('S', new Color(71, 0, 63), new Color(25, 0, 48)),

      new BiomeTerrain("MARSH", 2,
          new BiomeProp("TREE_SWAMP", 0.02),
          new BiomeProp("SHARKWEED", 0.1)),
      new BiomeTerrain("MUCK", 16,
          new BiomeProp("TREE_SWAMP", 0.02),
          new BiomeProp("UNDERGROWTH", 0.04),
          new BiomeProp("OOZE", 0.004)));

  public final Appearance worldMapAppearance;
  final int weight;
  final Set<BiomeTerrain> biomeTerrain;


  Biome(int weight, Appearance worldMapAppearance, BiomeTerrain... biomeTerrain) {
    this.weight = weight;
    this.worldMapAppearance = worldMapAppearance;
    this.biomeTerrain = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(biomeTerrain)));
  }


  public static Set<Biome> getAll() {
    return new HashSet<>(Arrays.asList(values()));
  }


  @Override
  public int getWeight() {
    return weight;
  }


  public Set<BiomeTerrain> getBiomeTerrain() {
    return biomeTerrain;
  }

}