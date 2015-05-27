package world;

import game.Physical;
import utils.Dimension;


/**
 * Contains the Physical information for the game. The way the world looks and the locations of
 * any placed Physicals. Many/most generic Things are stored only in the listings here.
 */
public final class Area {

  private final Biome biome;
  private final Dimension size;

  private final Component_Physicals physicals;

  Area(Biome biome, Dimension size, Terrain[][] terrain, Physical[][] physicals) {

    this.biome = biome;
    this.size = size;
    this.physicals = new Component_Physicals(this.size,terrain,physicals);

  }


  public Biome getBiome() {
    return biome;
  }

  public Dimension getSize() {
    return size;
  }

  public Component_Physicals getPhysicalsComponent() {
    return physicals;
  }

}