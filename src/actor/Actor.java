package actor;

import game.Game;
import game.Physical;

import java.awt.*;

/**
 * Actors are subjects. They act upon Things and other Actors in the world. The Actor class only
 * handles the physical state of the Actor. Behavior and actions are handled by a Controller.
 */
public class Actor implements Physical {

  private final char appearance;
  private final Color color;
  private final Color bgcolor;
  private final Double weight;

  private final boolean isBlocking;


  Actor(ActorTemplate aT) {
    appearance = aT.appearance;
    color = aT.color;
    bgcolor = aT.bgcolor;
    weight = aT.weight;
    isBlocking = aT.isBlocking;
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
    return bgcolor;
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
