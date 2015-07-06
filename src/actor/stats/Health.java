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

  public static final int HEALTH_PER_GRIT = 10;


  private final Actor actor;

  private int maximum;
  private int current;

  public Health(Actor actor) {
    this.actor = actor;
    maximum = actor.getAttributeRank(Attribute.GRIT).ordinal() * HEALTH_PER_GRIT;
    current = maximum;
  }


  public void heal(int healing) {

    // The dead cannot be healed.
    if (!actor.hasFlag(PhysicalFlag.DEAD)) {

      // Apply the healing.
      current += healing;

      // Enforce the maximum.
      if (current > maximum) {
        current = maximum;
      }

    }

  }

  /**
   * Inflict damage on this actor. If this reduces the actor's current health to zero or lower,
   * the actor is now dead.
   */
  public void wound(int damage) {

    // The dead cannot be wounded.
    if (!actor.hasFlag(PhysicalFlag.DEAD)) {

      // Apply the damage.
      current -= damage;

      // If the actor has run out of health, they are now dead.
      if (current <= 0) {

        // Construct a log message
        final String message;
        if (Game.getActiveInputSwitch().getPlayerController().getActor() == actor) {
          message = "You have died.";
        } else {
          message = actor.getName() + " has died.";
        }

        // Notify the player, if they are local.
        EventLog.registerEventIfPlayerIsNear(actor.getCoordinate(), Event.ACTOR_WOUNDED, message);

        // Notify the parent class.
        actor.die();

      }

    }

  }


  public int getCurrent() {
    return current;
  }

  public int getMaximum() {
    return maximum;
  }

  /**
   * @return A value between 0.0 and 1.0 that is equal to current health divided by maximum health.
   */
  public double getFraction() {
    return getCurrent() / getMaximum();
  }
  
}