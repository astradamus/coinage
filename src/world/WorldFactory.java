package world;

import utils.Array2D;
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
    Array2D<Area> areas = new Array2D<>(worldSizeInAreas);

    for (int y = 0; y < worldSizeInAreas.getHeight(); y++) {
      for (int x = 0; x < worldSizeInAreas.getWidth(); x++) {

        final Biome blueprintBiome = Biome.values()[biomeBlueprint.weightMap.get(x, y)];
        areas.put(AreaFactory.standardGeneration(blueprintBiome, areaSizeInSquares), x, y);
      }
    }

    areas = areas.unmodifiableView(worldSizeInAreas, 0, 0);

    return new World(areas, areaSizeInSquares);
  }
}