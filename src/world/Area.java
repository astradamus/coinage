package world;


/**
 *
 */
public final class Area {

  private final Biome biome;

  private final Square[][] squares;

  Area(Biome biome, Square[][] squares) {

    this.biome = biome;
    this.squares = squares;

  }

  public Biome getBiome() {
    return biome;
  }

  Square getSquare(AreaCoordinate coordinate) {
    return squares[coordinate.areaY][coordinate.areaX];
  }

}