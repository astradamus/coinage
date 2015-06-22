package world;

import game.physical.Physical;

/**
 *
 */
public class Terrain extends Physical {

  static final char[] chars = new char[]{'.', ',', '\'', '`'};

  Terrain(TerrainType type) {
    super(type.name().toLowerCase(), type.getRandomAppearance());
  }

}