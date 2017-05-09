package world;

import utils.Array2d;
import utils.Dimension;
import world.blueprint.Blueprint;
import world.blueprint.BlueprintFactory;

/**
 *
 */
public class WorldFactory {

    private static final int STDGEN_PATCH_RADIUS_LIMIT = 4;
    private static final double STDGEN_PATCH_PATCHINESS = 0.000; // % of patch candidates to discard.


    public static World standardGeneration(Dimension areaSizeInSquares, Dimension worldSizeInAreas) {

        // Get a Blueprint
        Blueprint<Biome> blueprint = BlueprintFactory
                .generateWithPatches(worldSizeInAreas, Biome.getAll(), STDGEN_PATCH_RADIUS_LIMIT,
                                     STDGEN_PATCH_PATCHINESS);

        // Produce areas from Blueprint.
        Array2d<Area> areas =
                blueprint.build().map(biome -> AreaFactory.standardGeneration(biome, areaSizeInSquares));

        return new World(areas.unmodifiableView(worldSizeInAreas, 0, 0), areaSizeInSquares);
    }
}