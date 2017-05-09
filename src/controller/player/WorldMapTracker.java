package controller.player;

import utils.Dimension;
import world.MapCoordinate;

public class WorldMapTracker {

    private final boolean[][] worldMapRevealed;


    public WorldMapTracker(Dimension worldSizeInAreas) {
        worldMapRevealed = new boolean[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
    }


    public boolean getAreaIsRevealed(MapCoordinate mapCoordinate) {
        return worldMapRevealed[mapCoordinate.worldAreasY][mapCoordinate.worldAreasX];
    }


    public void setAreaIsRevealed(MapCoordinate mapCoordinate) {
        worldMapRevealed[mapCoordinate.worldAreasY][mapCoordinate.worldAreasX] = true;
    }
}