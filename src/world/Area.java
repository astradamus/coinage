package world;

import utils.Array2D;

/**
 *
 */
public final class Area {

  private final Biome biome;

  private final Array2D<Square> squares;


  Area(Biome biome, Array2D<Square> squares) {

    this.biome = biome;
    this.squares = squares;
  }


  public Biome getBiome() {
    return biome;
  }


  Square getSquare(AreaCoordinate coordinate) {
    return squares.get(coordinate.areaX, coordinate.areaY);
  }
}