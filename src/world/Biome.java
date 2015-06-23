package world;

import game.physical.Appearance;

import java.awt.Color;

/**
 *
 */
public enum Biome {

  GRASSLAND(8, new Appearance('G', new Color(7, 140, 0), new Color(8, 96, 0))){
    {
      terrainTypes = new TerrainType[] { TerrainType.GRASS,   TerrainType.DIRT,   TerrainType.ROCK};
      terrainWeights = new int[]                       {40,                  3,                  1};
      featureIDs = new String[][] {               {"TREE"},           {"TREE"},          {"BOULDER",
                                                                                          "STONE"}};
      featureFrequencies = new double[][] {         {0.02},             {0.01},              {0.002,
                                                                                           0.0001}};
    }
  },
  FOREST(4, new Appearance('F', new Color(28, 82, 0), new Color(3, 33, 0))){
    {
      terrainTypes = new TerrainType[] { TerrainType.GRASS,   TerrainType.DIRT,   TerrainType.ROCK};
      terrainWeights = new int[]                       {32,                  2,                  1};
      featureIDs = new String[][] {                {"TREE",
                                            "UNDERGROWTH"},           {"TREE"},          {"BOULDER",
                                                                                          "STONE"}};
      featureFrequencies = new double[][] {         {0.12,
                                                     0.12},             {0.03},              {0.001,
                                                                                           0.0001}};
    }
  },
  CRAGS(2, new Appearance('C', new Color(205, 205, 205), new Color(85, 85, 85))){
    {
      terrainTypes = new TerrainType[] { TerrainType.GRASS,   TerrainType.DIRT,   TerrainType.ROCK};
      terrainWeights = new int[]                        {1,                  2,                 16};
      featureIDs = new String[][] {               {"TREE"},           {"TREE"},          {"BOULDER",
                                                                                          "STONE"}};
      featureFrequencies = new double[][] {        {0.004},            {0.002},               {0.15,
                                                                                             0.05}};
    }
  },

  DESERT(1, new Appearance('D', new Color(255, 204, 0), new Color(114, 90, 0))){
    {
      terrainTypes = new TerrainType[] {         TerrainType.SAND,     TerrainType.SANDSTONE};
      terrainWeights = new int[]                               {20,                        1};
      featureIDs = new String[][] {                        {"DUNE",
                                                         "CACTUS"},      {"BOULDER_SANDSTONE",
                                                                                   "CACTUS"}};
      featureFrequencies = new double[][] {                  {0.6,
                                                           0.005},                     {0.003,
                                                                                      0.002}};
    }
  },
  BADLANDS(1, new Appearance('B', new Color(144, 71, 0), new Color(76, 14, 0))){
    {
      terrainTypes = new TerrainType[] { TerrainType.DIRT,  TerrainType.SAND,  TerrainType.SANDSTONE};
      terrainWeights = new int[]                        {1,                 3,                    30};
      featureIDs = new String[][] {                     {},        {"CACTUS"},   {"BOULDER_SANDSTONE",
                                                                                           "CACTUS"}};
      featureFrequencies = new double[][] {             {},           {0.001},                  {0.05,
                                                                                              0.004}};
    }
  },

  SWAMP(1, new Appearance('S', new Color(71, 0, 63), new Color(25, 0, 48))){
    {
      terrainTypes = new TerrainType[] {  TerrainType.MARSH,  TerrainType.MUCK};
      terrainWeights = new int[]                         {2,                16};
      featureIDs = new String[][] {           {"TREE_SWAMP",
                                               "SHARKWEED"},      {"TREE_SWAMP",
                                                                  "UNDERGROWTH",
                                                                       "OOZE"}};
      featureFrequencies = new double[][] {            {0.02,
                                                      0.10},              {0.02,
                                                                           0.04,
                                                                        0.004}};
    }
  };

  public static int[] getAllWeights() {
    int[] weights = new int[values().length];
    for (int i = 0; i < weights.length; i++) {
      weights[i] = values()[i].biomeWeight;
    }
    return  weights;
  }

  final int biomeWeight;

  public final Appearance worldMapAppearance;

  TerrainType[] terrainTypes;
  int[] terrainWeights;
  String[][] featureIDs;
  double[][] featureFrequencies;

  Biome(int biomeWeight, Appearance worldMapAppearance) {
    this.biomeWeight = biomeWeight;
    this.worldMapAppearance = worldMapAppearance;
  }

}
