package controller.player;

import utils.Dimension;
import world.Coordinate;

public class Component_WorldMapRevealed {

  private final boolean[][] worldMapRevealed;

  public Component_WorldMapRevealed(Dimension worldSizeInAreas) {
    worldMapRevealed = new boolean[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
  }


  public boolean getAreaIsRevealed(Coordinate coordinate) {
    return worldMapRevealed[coordinate.worldY][coordinate.worldX];
  }

  public void setAreaIsRevealed(Coordinate coordinate) {
    worldMapRevealed[coordinate.worldY][coordinate.worldX] = true;
  }

}