package controller.player;

import utils.ImmutableDimension;
import world.MapCoordinate;

public class Component_WorldMapRevealed {

  private final boolean[][] worldMapRevealed;


  public Component_WorldMapRevealed(ImmutableDimension worldSizeInAreas) {
    worldMapRevealed = new boolean[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
  }


  public boolean getAreaIsRevealed(MapCoordinate mapCoordinate) {
    return worldMapRevealed[mapCoordinate.worldAreasY][mapCoordinate.worldAreasX];
  }


  public void setAreaIsRevealed(MapCoordinate mapCoordinate) {
    worldMapRevealed[mapCoordinate.worldAreasY][mapCoordinate.worldAreasX] = true;
  }
}