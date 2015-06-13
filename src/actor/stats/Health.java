package actor.stats;

import actor.Actor;
import actor.attribute.Attribute;
import game.display.Event;
import game.display.EventLog;

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
    if (actor.isDead()) {
      return; // The dead cannot be healed.
    }
    current += healing;
    if (current > maximum) {
      current = maximum;
    }
  }

  public void wound(double damage) {
    if (actor.isDead()) {
      return; // The dead cannot be wounded.
    }

    double remaining = current - damage;


    EventLog.registerEventIfPlayerIsNear(actor.getCoordinate(), Event.ACTOR_WOUNDED,
        actor.getName() + " suffered " + Double.toString(damage)+" damage.");

    if (remaining <= 0) {

      EventLog.registerEventIfPlayerIsNear(actor.getCoordinate(), Event.ACTOR_WOUNDED,
          actor.getName()+" has died.");

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

}