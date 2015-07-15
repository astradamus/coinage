package world;

import game.Game;
import game.io.GameResources;
import game.physical.Physical;
import thing.ThingFactory;
import utils.Array2D;
import utils.Dimension;
import world.blueprinter.Blueprint;
import world.blueprinter.BlueprintFactory;

import java.util.List;

/**
 *
 */
class AreaFactory {

  private static final int STDGEN_PATCH_RADIUS_LIMIT = 5;
  private static final double STDGEN_PATCH_PATCHINESS = 0.300; // % of patch candidates to discard.


  public static Area standardGeneration(Biome biome, Dimension areaSizeInSquares) {

    // Get a Blueprint
    final Blueprint terrainBlueprint =
        BlueprintFactory.generateWithCrawler(areaSizeInSquares, biome.getTerrainWeights());

    // Generate Features
    final Array2D<Physical> physicals =
        generateFeatures(biome, terrainBlueprint, areaSizeInSquares);

    // Produce square map from Blueprint
    final Array2D<Square> squares = new Array2D<>(areaSizeInSquares);

    for (int y = 0; y < areaSizeInSquares.getHeight(); y++) {
      for (int x = 0; x < areaSizeInSquares.getWidth(); x++) {

        final String terrainTypeID =
            biome.getTerrainTypeByIndex(terrainBlueprint.weightMap.get(x, y));
        final Terrain terrain =
            GameResources.getTerrainTypeByID(terrainTypeID).getRandomVariation();
        final Square square = new Square(terrain);
        final Physical physical = physicals.get(x, y);

        if (physical != null) {
          square.put(physical);
        }

        squares.put(square, x, y);
      }
    }

    return new Area(biome, squares);
  }


  private static Array2D<Physical> generateFeatures(Biome biome, Blueprint terrainBlueprint,
      Dimension dimension) {

    final Array2D<Physical> physicals = new Array2D<>(dimension);

    // For each terrain classification in the biome...
    final List<BiomeTerrain> biomeTerrainList = biome.getBiomeTerrainList();
    for (int terrainIndex = 0; terrainIndex < biomeTerrainList.size(); terrainIndex++) {
      final BiomeTerrain biomeTerrain = biomeTerrainList.get(terrainIndex);

      // For each feature classification in the terrain...
      final List<BiomeProp> features = biomeTerrain.getBiomeProps();
      for (int featureIndex = 0; featureIndex < features.size(); featureIndex++) {
        final BiomeProp biomeProp = features.get(featureIndex);

        // Retrieve the feature ID and calculate how many of the feature we should try to place.
        final String featureID = biomeProp.getThingTemplateID();
        int featureCount =
            (int) (terrainBlueprint.distribution[terrainIndex] * biomeProp.getFrequency());

        // Pick random points until we've picked a matching terrain that has no physicals, then
        // place the feature and continue the search--until we've placed all the features or we've
        // searched for too long.
        int searchLimit = 10000;
        while (featureCount > 0 && searchLimit > 0) {
          searchLimit--;

          int x = Game.RANDOM.nextInt(dimension.getWidth());
          int y = Game.RANDOM.nextInt(dimension.getHeight());

          if (terrainBlueprint.weightMap.get(x, y) == terrainIndex && physicals.get(x, y) == null) {
            physicals.put(ThingFactory.makeThing(featureID), x, y);
            featureCount--;
            searchLimit = 10000;
          }
        }
        if (searchLimit == 0) {
          System.out.println("Failed to find spot for " + featureID + ". Frequency too high?");
        }
      }
    }

    return physicals;
  }
}