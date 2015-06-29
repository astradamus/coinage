package controller.ai;

import controller.ActorController;
import controller.action.Action;
import controller.action.ActionFlag;
import game.Direction;
import game.Game;
import game.physical.PhysicalFlag;
import world.Coordinate;

/**
 * This behavior will make the puppet move at full speed away from the given pursuer. The puppet
 * will attempt to place a distance of two areas between itself and its pursuer before it stops
 * running. However, if it is determined that the puppet can't escape, it will turn and fight
 * instead.<br><br>
 *
 * This determination is made by recording the occurrences of three events. First, if the puppet
 * is victimized by the pursuer while attempting to flee. Second, if the puppet has to change the
 * direction of its escape, because the pursuer is outpacing or outmaneuvering it. Third, if the
 * puppet's path of escape is blocked. Each of these events adds a certain (statically defined)
 * value to a counter, and when that counter reaches a certain (statically defined) point, the
 * puppet will turn and fight.
 */
public class AI_Retreat extends AIBehavior {

  public static final int CANT_ESCAPE_COUNTER_MAX = 30;

  private static final int CANT_ESCAPE_ATTACKED_VALUE = 20;
  private static final int CANT_ESCAPE_REDIRECTED_VALUE = 8;
  private static final int CANT_ESCAPE_BLOCKED_VALUE = 1;


  private final ActorController pursuer;
  private int cantEscapeCounter;
  private boolean isTurningToEscape;

  public AI_Retreat(AIController puppet, ActorController pursuer) {
    super(puppet);
    this.pursuer = pursuer;
    cantEscapeCounter = 0;
    isTurningToEscape = false;
  }

  @Override
  protected String getOnExhibitLogMessage() {
    if (pursuer == Game.getActivePlayer()) {
      return getPuppet().getActor().getName() + " flees for its life.";
    }
    else {
      return null;
    }
  }

  @Override
  protected void onExhibit() {
    retreat();
  }


  private void retreat() {

    // If our pursuer is dead, we can stop retreating.
    if (pursuer.getActor().hasFlag(PhysicalFlag.DEAD)) {
      markComplete();
    }

    else {

      final Coordinate actorAt = getPuppet().getActor().getCoordinate();
      final Coordinate attackerAt = pursuer.getActor().getCoordinate();


      // If we are two areas away we've escaped and can stop retreating.
      if (actorAt.getWorldDistance(attackerAt) >= 2) {
        markComplete();
      }

      // Otherwise, run directly away.
      else {
        final Direction toEscape = attackerAt.getDirectionTo(actorAt);
        AIRoutines.turnThenMove(getPuppet(), toEscape, false, false);
      }

    }


  }

  private void incrementCantEscapeCounter(int increments) {

    // Increment the can't escape counter and then check if the counter has hit its limit. If so,
    // give up on trying to escape, and turn to fight our pursuer.
    cantEscapeCounter += increments;

    if (cantEscapeCounter >= CANT_ESCAPE_COUNTER_MAX) {
      getPuppet().exhibitBehavior(new AI_Fight(getPuppet(), pursuer));
    }

  }


  @Override
  public void onActorTurnComplete() {

    // If we have no action queued, retreat directly away from our pursuer.
    if (getPuppet().isFreeToAct()) {
      retreat();
    }

    // Otherwise, if our pursuer is very close and we are not facing (or turning to face) the
    // best escape direction, renew our retreat, ensuring our direction is directly away from our
    // pursuer.
    else {

      final Coordinate actorAt = getPuppet().getActor().getCoordinate();
      final Coordinate attackerAt = pursuer.getActor().getCoordinate();

      final boolean pursuerIsClose = actorAt.getGlobalDistance(attackerAt) <= 3;

      if (pursuerIsClose) {

        final Direction escapeDirection = attackerAt.getDirectionTo(actorAt);
        final boolean facingEscapeDirection = getPuppet().getActor().getFacing() == escapeDirection;

        // If we are facing correct direction, any turning can stop.
        if (facingEscapeDirection) {
          isTurningToEscape = false;
        }

        // Otherwise, if we are not already turning to escape, do so now.
        else if (!isTurningToEscape) {
          retreat();
          incrementCantEscapeCounter(CANT_ESCAPE_REDIRECTED_VALUE);
          isTurningToEscape = true;
        }

      }

    }

  }

  @Override
  public void onActionExecuted(Action action) {

    // If one of our movements fails, step around the blocked square, and increment the can't
    // escape counter by the appropriate value.
    if (action.hasFlag(ActionFlag.FAILED)) {
      AIRoutines.stepAroundBlockedSquare(getPuppet());
      incrementCantEscapeCounter(CANT_ESCAPE_BLOCKED_VALUE);
    }

  }

  @Override
  public void onVictimized(ActorController attacker) {

    // If we are victimized by the same actor from whom we are retreating, increment the can't
    // escape counter by CANT_ESCAPE_ATTACKED_VALUE.
    if (this.pursuer == attacker) {
      incrementCantEscapeCounter(CANT_ESCAPE_ATTACKED_VALUE);
    }

  }

}