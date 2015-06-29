package actor.stats;

import actor.Actor;
import actor.attribute.Attribute;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import game.physical.PhysicalFlag;

/**
 * Defines how much damage an actor can take before dying and how tracks how much damage an actor
 * has already taken. Handles calls to damage or heal the actor, and notifies the parent actor
 * class when its health has been depleted and it should die. If the dying actor is local to the
 * player, a message will be reported to the event log.
 */
public class Health {

  private final Actor actor;

  private int maximum;
  private int current;

  public Health(Actor actor) {
    this.actor = actor;
    maximum = actor.readAttributeLevel(Attribute.GRIT).ordinal()*10;
    current = maximum;
  }


  public void heal(int healing) {
    if (actor.hasFlag(PhysicalFlag.DEAD)) {
      return; // The dead cannot be healed.
    }
    current += healing;
    if (current > maximum) {
      current = maximum;
    }
  }

  public void wound(int damage) {
    if (actor.hasFlag(PhysicalFlag.DEAD)) {
      return; // The dead cannot be wounded.
    }

    int remaining = current - damage;

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


  public int getMaximum() {
    return maximum;
  }

  public int getCurrent() {
    return current;
  }

  public double getFraction() {
    return getCurrent() / getMaximum();
  }
  
}