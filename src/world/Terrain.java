package world;

import game.physical.Physical;
import game.physical.PhysicalFlag;

/**
 *
 */
class Terrain extends Physical {

  static final char[] chars = new char[]{'.', ',', '\'', '`'};

  Terrain(TerrainType type) {
    super(type.name().toLowerCase(), type.getRandomAppearance());
    addFlag(PhysicalFlag.IMMOVABLE);
  }

}