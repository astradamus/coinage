package controller.player;

import game.Game;
import utils.Dimension;
import world.Coordinate;
import world.WorldMapCoordinate;

public class Component_WorldMapRevealed {

  private final boolean[][] worldMapRevealed;

  public Component_WorldMapRevealed(Dimension worldSizeInAreas) {
    worldMapRevealed = new boolean[worldSizeInAreas.getHeight()][worldSizeInAreas.getWidth()];
  }


  public boolean getAreaIsRevealed(Coordinate coordinate) {
    WorldMapCoordinate worldMapCoordinate = Game.getActiveWorld().convertToWorldMapCoordinate(coordinate);
    return worldMapRevealed[worldMapCoordinate.worldAreasY][worldMapCoordinate.worldAreasX];
  }

  public void setAreaIsRevealed(Coordinate coordinate) {
    WorldMapCoordinate worldMapCoordinate = Game.getActiveWorld().convertToWorldMapCoordinate(coordinate);
    worldMapRevealed[worldMapCoordinate.worldAreasY][worldMapCoordinate.worldAreasX] = true;
  }

}