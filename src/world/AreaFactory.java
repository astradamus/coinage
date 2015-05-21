package world;

import game.Game;
import game.Physical;
import thing.ThingFactory;

/**
 *
 */
public class AreaFactory {

  public static final int STDGEN_PATCH_RADIUS_LIMIT = 3;
  public static final TerrainType STDGEN_BASE_TILETYPE = TerrainType.GRASS;
  public static final double STDGEN_ROCK_FREQUENCY = 0.020; // % of tiles are rock patch centers
  public static final double STDGEN_DIRT_FREQUENCY = 0.100; // % of tiles are dirt patch centers
  public static final double STDGEN_BOULDER_FREQUENCY = 0.002; // % of tiles are boulders
  public static final double STDGEN_TREE_FREQUENCY = 0.02; // % of tiles are trees
  public static final double STDGEN_PATCH_HOLINESS = 0.500; // % of patch candidates are discarded

  public static Area standardGeneration(int width, int height) {

    // FIRST, GENERATE TERRAIN

    Terrain[][] terrain = new Terrain[width][height];
    int rockPatchCount = (int) (STDGEN_ROCK_FREQUENCY * width * height);
    int dirtPatchCount = (int) (STDGEN_DIRT_FREQUENCY * width * height);

    // the loop: go back and forth placing rock and dirt patches at random (they can overlap),
    // one of each at a time. When one count runs out, finish the rest of the other count.

    boolean rockPatchNext = Game.RANDOM.nextBoolean();
    while (rockPatchCount > 0 || dirtPatchCount > 0) {

      TerrainType placingType;

      if (rockPatchNext) {
        placingType = TerrainType.ROCK;
        rockPatchCount--;
      } else {
        placingType = TerrainType.DIRT;
        dirtPatchCount--;
      }

      placePatch(terrain, placingType, Game.RANDOM.nextInt(width), Game.RANDOM.nextInt(height));

      if (rockPatchCount <= 0) {
        rockPatchNext = false;
      } else if (dirtPatchCount <= 0) {
        rockPatchNext = true;
      } else {
        rockPatchNext = !rockPatchNext;
      }

    }

    // after the loop: fill all null spots with the base TerrainType
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        if (terrain[y][x] == null) {
          terrain[y][x] = new Terrain(STDGEN_BASE_TILETYPE);
        }
      }
    }


    // SECOND, GENERATE THINGS

    Physical[][] physicals = new Physical[width][height];
    int treeCount = (int) (STDGEN_TREE_FREQUENCY * width * height);
    int boulderCount = (int) (STDGEN_BOULDER_FREQUENCY * width * height);

    // generate trees
    treeLoop:
    while (treeCount > 0) {
      treeCount--;

      // pick random points until picking a grass terrain that has no physicals
      int x = Game.RANDOM.nextInt(width);
      int y = Game.RANDOM.nextInt(height);
      int loopLimit = 10000;
      while (terrain[y][x].getType() != TerrainType.GRASS || physicals[y][x] != null) {
        x = Game.RANDOM.nextInt(width);
        y = Game.RANDOM.nextInt(height);
        loopLimit--;
        if (loopLimit == 0) {
          System.out.println("Failed to find spot for TREE. Frequency too high?");
          break treeLoop;
        }
      }
      physicals[y][x] = ThingFactory.makeThing("TREE");
    }

    // generate boulders
    boulderLoop:
    while (boulderCount > 0) {
      boulderCount--;

      // pick random points until picking a rock terrain that has no physicals
      int x = Game.RANDOM.nextInt(width);
      int y = Game.RANDOM.nextInt(height);
      int loopLimit = 10000;
      while (terrain[y][x].getType() != TerrainType.ROCK || physicals[y][x] != null) {
        x = Game.RANDOM.nextInt(width);
        y = Game.RANDOM.nextInt(height);
        loopLimit--;
        if (loopLimit == 0) {
          System.out.println("Failed to find spot for BOULDER. Frequency too high?");
          break boulderLoop;
        }
      }
      physicals[y][x] = ThingFactory.makeThing("BOULDER");
    }

    return new Area(terrain, physicals);
  }

  /**
   * Generate terrain for each spot within a randomly sized range (limited by
   * STDGEN_PATCH_RADIUS_LIMIT) around the given x/y coordinate. For each spot in range, there is
   * a STDGEN_PATCH_HOLINESS chance of no terrain being generated.
   */
  private static void placePatch(Terrain[][] targetTerrain, TerrainType placingType, int x, int y) {

    // decide how big this patch will be
    int patchRadius = Game.RANDOM.nextInt(STDGEN_PATCH_RADIUS_LIMIT);

    // loop through all candidate terrain slots within the patch
    for(int adjY = y-patchRadius; adjY < y+patchRadius; adjY++) {
      for(int adjX = x-patchRadius; adjX < x+patchRadius; adjX++) {

        // don't go outside map boundaries
        if (adjX < 0 || adjX >= targetTerrain.length
            || adjY < 0 || adjY >= targetTerrain[0].length) {
          continue;
        }

        // check if this spot represents a hole in the patch
        if (Game.RANDOM.nextDouble() > STDGEN_PATCH_HOLINESS) {
          targetTerrain[adjX][adjY] = new Terrain(placingType); // if not, generate the terrain
        }

      }
    }
  }


}
