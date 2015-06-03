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

  public Square getSquare(Coordinate coordinate) {
    if (coordinate.area != this) {
      return null; // This coordinate does not point to a square in this area.
    }
    return squares[coordinate.localY][coordinate.localX];
  }

}