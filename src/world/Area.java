package world;

import utils.Array2d;

/**
 *
 */
public final class Area {

    private final Biome biome;

    private final Array2d<Square> squares;


    Area(Biome biome, Array2d<Square> squares) {

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