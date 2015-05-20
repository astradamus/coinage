package world;

import game.Game;
import game.Physical;

import java.awt.*;

/**
 *
 */
public class Terrain implements Physical {

  private final TerrainType type;
  private final int charID;
  private final int colorID;

  Terrain(TerrainType type) {
    this.type = type;
    this.charID = type.getRandomCharID();
    this.colorID = type.getRandomColorID();
  }

  public TerrainType getType() {
    return type;
  }

  @Override
  public char getAppearance() {
    return type.chars[charID];
  }

  @Override
  public Color getColor() {
    return type.colors[colorID];
  }

  @Override
  public Color getBGColor() {
    return null;
  }

  @Override
  public Double getWeight() {
    return Double.MAX_VALUE;
  }

  @Override
  public int getVisualPriority() {
    return Game.VISUAL_PRIORITY__TERRAIN;
  }

  @Override
  public boolean isImmovable() {
    return true;
  }

  @Override
  public boolean isBlocking() {
    return false;
  }

}
