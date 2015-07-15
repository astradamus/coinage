package world;

import utils.Dimension;
import world.blueprinter.Blueprint;
import world.blueprinter.BlueprintFactory;

/**
 *
 */
public class WorldFactory {

  private static final int    STDGEN_PATCH_RADIUS_LIMIT = 4;
  private static final double STDGEN_PATCH_PATCHINESS = 0.000; // % of patch candidates to discard.


  public static World standardGeneration(Dimension areaSizeInSquares, Dimension worldSizeInAreas) {

    // Get a Blueprint
    Blueprint biomeBlueprint = BlueprintFactory
        .generateWithPatches(worldSizeInAreas, Biome.getAllWeights(), STDGEN_PATCH_RADIUS_LIMIT,
            STDGEN_PATCH_PATCHINESS);

    // Produce areas from Blueprint.
    Area[][] areas = new Area[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
    for (int y = 0; y < worldSizeInAreas.getHeight(); y++) {
      for (int x = 0; x < worldSizeInAreas.getWidth(); x++) {
        areas[y][x] = AreaFactory
            .standardGeneration(Biome.values()[biomeBlueprint.weightMap[y][x]], areaSizeInSquares);
      }
    }

    return new World(areas, areaSizeInSquares);
  }
}