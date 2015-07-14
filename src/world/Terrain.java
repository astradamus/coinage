package world;

import game.physical.Physical;
import game.physical.PhysicalFlag;

/**
 *
 */
class Terrain extends Physical {

  Terrain(TerrainType type) {
    super(type.name, type.getRandomAppearance());
    addFlag(PhysicalFlag.IMMOVABLE);
  }

}