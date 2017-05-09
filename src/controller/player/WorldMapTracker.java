package controller.player;

import utils.Dimension;
import world.WorldMapCoordinate;

public class WorldMapTracker {

    private final boolean[][] worldMapRevealed;


    public WorldMapTracker(Dimension worldSizeInAreas) {
        worldMapRevealed = new boolean[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
    }


    public boolean getAreaIsRevealed(WorldMapCoordinate worldMapCoordinate) {
        return worldMapRevealed[worldMapCoordinate.worldAreasY][worldMapCoordinate.worldAreasX];
    }


    public void setAreaIsRevealed(WorldMapCoordinate worldMapCoordinate) {
        worldMapRevealed[worldMapCoordinate.worldAreasY][worldMapCoordinate.worldAreasX] = true;
    }
}