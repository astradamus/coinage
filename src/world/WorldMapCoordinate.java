package world;

/**
 *
 */
public class WorldMapCoordinate {

    public final int worldAreasX;
    public final int worldAreasY;

    WorldMapCoordinate(int worldAreasX, int worldAreasY) {
        this.worldAreasX = worldAreasX;
        this.worldAreasY = worldAreasY;
    }

    public WorldMapCoordinate offset(int offX, int offY) {
        return new WorldMapCoordinate(worldAreasX + offX, worldAreasY + offY);
    }

    /**
     * @return The Chebyshev/"Chessboard" distance between this and another coordinate.
     */
    public int getDistance(WorldMapCoordinate target) {

        final int deltaX = Math.abs(this.worldAreasX - target.worldAreasX);
        final int deltaY = Math.abs(this.worldAreasY - target.worldAreasY);

        return Math.max(deltaX, deltaY);

    }
}