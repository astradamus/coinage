package controller.player;

import utils.Dimension;
import world.MapCoordinate;

public class Component_WorldMapRevealed {

  private final boolean[][] worldMapRevealed;

  public Component_WorldMapRevealed(Dimension worldSizeInAreas) {
    worldMapRevealed = new boolean[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
  }


  public boolean getAreaIsRevealed(MapCoordinate mapCoordinate) {
    return worldMapRevealed[mapCoordinate.worldAreasY][mapCoordinate.worldAreasX];
  }

  public void setAreaIsRevealed(MapCoordinate mapCoordinate) {
    worldMapRevealed[mapCoordinate.worldAreasY][mapCoordinate.worldAreasX] = true;
  }

}