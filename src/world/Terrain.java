package world;

import game.Game;
import game.Physical;

import java.awt.*;

/**
 *
 */
public class Terrain implements Physical {

  private static char[] chars = new char[]{'.', ',', '\'', '`'};

  private final TerrainType type;
  private final char appearance;
  private final Color color;

  Terrain(TerrainType type) {
    this.type = type;
    this.appearance = chars[Game.RANDOM.nextInt(chars.length)];
    this.color = type.colors[Game.RANDOM.nextInt(type.colors.length)];
  }


  public TerrainType getType() {
    return type;
  }

  @Override
  public String getName() {
    return type.name().substring(0,1) + type.name().toLowerCase().substring(1); // cap first char
  }

  @Override
  public char getAppearance() {
    return appearance;
  }

  @Override
  public Color getColor() {
    return color;
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
