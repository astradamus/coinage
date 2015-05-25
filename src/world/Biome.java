package world;

/**
 *
 */
public enum Biome {

  GRASSLAND(8){
    {
      terrainTypes = new TerrainType[] { TerrainType.GRASS,   TerrainType.DIRT,   TerrainType.ROCK};
      terrainWeights = new int[]                       {40,                  3,                  1};
      featureIDs = new String[][] {               {"TREE"},           {"TREE"},        {"BOULDER"}};
      featureFrequencies = new double[][] {         {0.02},             {0.01},            {0.002}};
    }
  },
  WOODS(4){
    {
      terrainTypes = new TerrainType[] { TerrainType.GRASS,   TerrainType.DIRT,   TerrainType.ROCK};
      terrainWeights = new int[]                       {32,                  2,                  1};
      featureIDs = new String[][] {                {"TREE",
                                             "OVERGROWTH"},           {"TREE"},        {"BOULDER"}};
      featureFrequencies = new double[][] {         {0.12,
                                                     0.12},             {0.03},            {0.001}};
    }
  },
  CRAGS(2){
    {
      terrainTypes = new TerrainType[] { TerrainType.GRASS,   TerrainType.DIRT,   TerrainType.ROCK};
      terrainWeights = new int[]                        {1,                  2,                 16};
      featureIDs = new String[][] {               {"TREE"},           {"TREE"},        {"BOULDER"}};
      featureFrequencies = new double[][] {        {0.004},            {0.002},             {0.15}};
    }
  },

  DESERT(1){
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
  BADLANDS(1){
    {
      terrainTypes = new TerrainType[] { TerrainType.DIRT,  TerrainType.SAND,  TerrainType.SANDSTONE};
      terrainWeights = new int[]                        {1,                 3,                    30};
      featureIDs = new String[][] {                     {},        {"CACTUS"},   {"BOULDER_SANDSTONE",
                                                                                           "CACTUS"}};
      featureFrequencies = new double[][] {             {},           {0.001},                  {0.05,
                                                                                              0.004}};
    }
  },

  SWAMP(1){
    {
      terrainTypes = new TerrainType[] {  TerrainType.MARSH,  TerrainType.MUCK};
      terrainWeights = new int[]                         {2,                16};
      featureIDs = new String[][] {           {"TREE_SWAMP",
                                               "SHARKWEED"},      {"TREE_SWAMP",
                                                                   "OVERGROWTH",
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

  TerrainType[] terrainTypes;
  int[] terrainWeights;
  String[][] featureIDs;
  double[][] featureFrequencies;

  Biome(int biomeWeight) {
    this.biomeWeight = biomeWeight;
  }
}
