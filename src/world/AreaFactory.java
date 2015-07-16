package world;

import game.Game;
import game.physical.Physical;
import thing.ThingFactory;
import utils.Array2D;
import utils.Dimension;
import world.blueprint.Blueprint;
import world.blueprint.BlueprintFactory_Crawler;

/**
 *
 */
class AreaFactory {

  private static final int STDGEN_PATCH_RADIUS_LIMIT = 5;
  private static final double STDGEN_PATCH_PATCHINESS = 0.300; // % of patch candidates to discard.


  public static Area standardGeneration(Biome biome, Dimension areaSizeInSquares) {

    // Get a Blueprint
    final Blueprint<BiomeTerrain> terrainBlueprint =
        BlueprintFactory_Crawler.generate(areaSizeInSquares, biome.getBiomeTerrain());

    // Generate Props
    final Array2D<Physical> physicals = generateProps(biome, terrainBlueprint, areaSizeInSquares);

    // Produce square map from Blueprint
    final Array2D<Square> squares =
        terrainBlueprint.build().map(biomeTerrain -> new Square(biomeTerrain.getTerrainTypeID()));

    // Add props to square map.
    for (int y = 0; y < areaSizeInSquares.getHeight(); y++) {
      for (int x = 0; x < areaSizeInSquares.getWidth(); x++) {
        final Physical physical = physicals.get(x, y);
        if (physical != null) {
          squares.get(x, y).put(physical);
        }
      }
    }

    return new Area(biome, squares.unmodifiableView(squares.getDimension(), 0, 0));
  }


  private static Array2D<Physical> generateProps(Biome biome, Blueprint<BiomeTerrain> blueprint,
      Dimension dimension) {

    final Array2D<Physical> physicals = new Array2D<>(dimension);

    // For each terrain classification in the biome...
    for (BiomeTerrain biomeTerrain : biome.getBiomeTerrain()) {

      // For each prop classification in the terrain...
      for (BiomeProp biomeProp : biomeTerrain.getBiomeProps()) {

        // Retrieve the feature ID and calculate how many of the feature we should try to place.
        final String featureID = biomeProp.getThingTemplateID();
        int featureCount =
            (int) (blueprint.getFeatureCount(biomeTerrain) * biomeProp.getFrequency());

        // Pick random points until we've picked a matching terrain that has no physicals, then
        // place the feature and continue the search--until we've placed all the features or we've
        // searched for too long.
        int searchLimit = 10000;
        while (featureCount > 0 && searchLimit > 0) {
          searchLimit--;

          int x = Game.RANDOM.nextInt(dimension.getWidth());
          int y = Game.RANDOM.nextInt(dimension.getHeight());

          if (blueprint.get(x, y) == biomeTerrain && physicals.get(x, y) == null) {
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