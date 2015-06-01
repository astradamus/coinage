package controller.player;

import utils.Dimension;

public class Component_WorldMapRevealed {

  private final boolean[][] worldMapRevealed;

  public Component_WorldMapRevealed(Dimension worldSizeInAreas) {
    worldMapRevealed = new boolean[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
  }


  public boolean getAreaIsRevealed(int worldX, int worldY) {
    return worldMapRevealed[worldY][worldX];
  }

  public void setAreaIsRevealed(int worldX, int worldY) {
    worldMapRevealed[worldY][worldX] = true;
  }

}