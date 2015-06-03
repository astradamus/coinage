package world;

import game.Game;
import game.Physical;
import game.display.Appearance;

import java.awt.*;

/**
 *
 */
public class Terrain implements Physical {

  private static char[] chars = new char[]{'.', ',', '\'', '`'};

  private final TerrainType type;
  private final Appearance appearance;

  Terrain(TerrainType type) {

    this.type = type;

    char character = chars[Game.RANDOM.nextInt(chars.length)];
    Color color = type.colors[Game.RANDOM.nextInt(type.colors.length)];

    this.appearance = new Appearance(character,color);

  }


  public TerrainType getType() {
    return type;
  }

  @Override
  public String getName() {
    return type.name().toLowerCase();
  }

  @Override
  public Appearance getAppearance() {
    return appearance;
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
