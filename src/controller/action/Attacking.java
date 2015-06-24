package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import controller.ActorController;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import world.Coordinate;

import java.awt.Color;

/**
 * Actors perform attacks to inflict damage upon other actors.
 */
public class Attacking extends Action {



  private final ActorController intendedVictim;
  private ActorController actualVictim;

  public Attacking(ActorController performer, Coordinate victimWhere) {
    super(performer, victimWhere);
    this.intendedVictim = Game.getActiveControllers().getActorControllerAt(victimWhere);
  }

  @Override
  public Color getIndicatorColor() {
    return Color.RED;
  }



  @Override
  public int calcDelayToPerform() {
    return 3;
  }

  @Override
  public int calcDelayToRecover() {
    return 2;
  }



  /**
   * Attacking will fail if the intendedVictim is already dead or if the intendedVictim is no longer at the
   * target location.
   */
  @Override
  protected boolean validate() {

    if (intendedVictim != null) {

      final boolean intendedVictimHasMoved =
          !getTarget().getSquare().getAll().contains(intendedVictim.getActor());

      if (!intendedVictimHasMoved) {
        // Our intended victim is right where we want them.
        actualVictim = intendedVictim;
      }

    }


    // If actualVictim is still null, then we either had no intended victim, or they're gone.
    final boolean intendedVictimHasMovedOrWasNotProvided = actualVictim == null;

    if (intendedVictimHasMovedOrWasNotProvided) {
      // Check if there's a new victim in the target square for the attack to hit.
      actualVictim = Game.getActiveControllers().getActorControllerAt(getTarget());
    }



    // If actualVictim is STILL null, then this attack is a miss.
    final boolean attackWillHitSomeone = actualVictim != null;

    if (!attackWillHitSomeone && getPlayerIsPerformer()) {
        if (intendedVictim == null) {
          EventLog.registerEvent(Event.INVALID_ACTION, "Your strike hits naught but air.");
        } else {
          EventLog.registerEvent(Event.INVALID_ACTION,
              intendedVictim.getActor().getName() + " eluded your attack.");
        }
    }

    return attackWillHitSomeone;

  }


  /**
   * Wound the victim, with severity based on the actor's muscle attribute. Damage ranges from
   * {@code muscle*2} to {@code muscle*5}.
   */
  @Override
  protected void apply() {

    final Actor actualVictimActor = actualVictim.getActor();

    final int actorMuscleRank = getPerformer().getActor()
        .readAttributeLevel(Attribute.MUSCLE).ordinal();

    final int damageBase  = actorMuscleRank * 2;
    final int damageRange = actorMuscleRank * 5;

    final double damage = damageBase + Game.RANDOM.nextInt(damageRange);

    String message = "struck "+ actualVictimActor.getName() + " for " + Double.toString(damage) + " damage.";

    if (getPlayerIsPerformer()) {
      message = "You have " + message;
    } else {
      message = getPerformer().getActor().getName() + " has "+ message;
    }

    EventLog.registerEventIfPlayerIsNear(actualVictimActor.getCoordinate(), Event.ACTOR_WOUNDED, message);

    actualVictimActor.getHealth().wound(damage);
    actualVictim.onVictimized(getPerformer());

  }


}