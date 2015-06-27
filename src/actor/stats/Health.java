package actor.stats;

import actor.Actor;
import actor.attribute.Attribute;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import game.physical.PhysicalFlag;

/**
 *
 */
public class Health {

  private final Actor actor;

  private double maximum;
  private double current;

  public Health(Actor actor) {
    this.actor = actor;
    maximum = actor.readAttributeLevel(Attribute.GRIT).ordinal()*10.0;
    current = maximum;
  }

  public void heal(double healing) {
    if (actor.hasFlag(PhysicalFlag.DEAD)) {
      return; // The dead cannot be healed.
    }
    current += healing;
    if (current > maximum) {
      current = maximum;
    }
  }

  public void wound(double damage) {
    if (actor.hasFlag(PhysicalFlag.DEAD)) {
      return; // The dead cannot be wounded.
    }

    double remaining = current - damage;

    if (remaining <= 0) {

      final String message;

      if (Game.getActivePlayer().getActor() == actor) {
        message = "You have died.";
      } else {
        message = actor.getName() + " has died.";
      }

      EventLog.registerEventIfPlayerIsNear(actor.getCoordinate(), Event.ACTOR_WOUNDED, message);

      actor.die();

    }

    current = remaining;

  }

  public double getMaximum() {
    return maximum;
  }

  public double getCurrent() {
    return current;
  }

  public double getFraction() {
    return getCurrent() / getMaximum();
  }
  
}