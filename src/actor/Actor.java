package actor;

import game.Game;
import game.Physical;
import game.display.Appearance;

import java.awt.*;

/**
 * Actors are subjects. They act upon Things and other Actors in the world. The Actor class only
 * handles the physical state of the Actor. Behavior and actions are handled by a Controller.
 */
public class Actor implements Physical {

  private final String name;
  private final Appearance appearance;
  private final Double weight;

  private final boolean isBlocking;


  Actor(ActorTemplate aT) {
    name = aT.name;

    char character = aT.appearance;
    Color color = aT.color;
    Color bgColor = aT.bgColor;
    this.appearance = new Appearance(character,color,bgColor);

    weight = aT.weight;
    isBlocking = aT.isBlocking;
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
    return Game.VISUAL_PRIORITY__ACTORS;
  }

  @Override
  public boolean isImmovable() {
    return false;
  }

  @Override
  public boolean isBlocking() {
    return isBlocking;
  }

}
