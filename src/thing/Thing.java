package thing;

import game.Game;
import game.Physical;
import game.display.Appearance;

import java.awt.*;

/**
 * {@code Things}s are objects. They are acted upon by {@code Actor}s, taking no actions of their
 * own and knowing nothing about the world around them.
 */
public class Thing implements Physical {

  private final String name;
  private final Appearance appearance;
  private final Double weight;

  private final boolean isImmovable;
  private final boolean isBlocking;


  Thing(ThingTemplate tT) {

    name = tT.name;
    this.appearance = new Appearance(tT.getRandomCharacter(),tT.getRandomColor());
    weight = tT.weight;
    isImmovable = tT.isImmovable;
    isBlocking = tT.isBlocking;

  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Appearance getAppearance() {
    return appearance;
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
    return isImmovable;
  }

  @Override
  public boolean isBlocking() {
    return isBlocking;
  }

}
