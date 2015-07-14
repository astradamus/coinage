package world;

import game.physical.Appearance;
import game.physical.Physical;
import game.physical.PhysicalFlag;

/**
 *
 */
class Terrain extends Physical {

  Terrain(String name, Appearance appearance) {
    super(name, appearance);
    addFlag(PhysicalFlag.IMMOVABLE);
  }

}