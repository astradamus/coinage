package thing;

import game.Game;
import game.Physical;

import java.awt.*;

/**
 * {@code Things}s are objects. They are acted upon by {@code Actor}s, taking no actions of their
 * own and knowing nothing about the world around them.
 */
public class Thing implements Physical {

  private final String name;
  private final char appearance;
  private final Color color;
  private final Double weight;

  private final boolean isImmobile;
  private final boolean isBlocking;


  Thing(ThingTemplate tT) {
    name = tT.name;
    appearance = tT.getRandomAppearance();
    color = tT.getRandomColor();
    weight = tT.weight;
    isImmobile = tT.isImmobile;
    isBlocking = tT.isBlocking;
  }

  @Override
  public String getName() {
    return name;
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
    return weight;
  }

  @Override
  public int getVisualPriority() {
    return Game.VISUAL_PRIORITY__THINGS;
  }

  @Override
  public boolean isImmovable() {
    return isImmobile;
  }

  @Override
  public boolean isBlocking() {
    return isBlocking;
  }

}
