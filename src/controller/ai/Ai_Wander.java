package controller.ai;

import actor.Actor;
import controller.action.Action;
import controller.action.ActionFlag;
import game.Direction;
import game.Game;

/**
 *  This behavior will make the agent wander to a random point, not by picking a point and
 *  pathing to it, but by chaining together a random series of movements. We pick a direction and
 *  start moving in it. If we hit a blocked square we immediately pick a new direction. Each time
 *  we move without hitting a blocked square, there's a chance we will still pick a new direction
 *  anyways. Each wander will last for a limited number of direction changes, and is complete
 *  when that number is fulfilled.<br><br>
 *
 *  Note that a sensory scan will be performed after each completed movement, but not in between
 *  them.
 */
public class Ai_Wander extends Behavior {

  public static final int WANDER_CHAIN_MAX_LENGTH = 3;


  private int wanderChain;

  public Ai_Wander(AiActorAgent agent) {
    super(agent);
    wanderChain = Game.RANDOM.nextInt(WANDER_CHAIN_MAX_LENGTH) + 1;
  }

  @Override
  protected void onExhibit() {
    wander();
  }


  private void wander() {

    // If we've completed enough wander steps, we can stop wandering.
    if (wanderChain <= 0) {
      markComplete();
    }

    else {

      // Pick a direction at random and start walking that way.
      final Direction randomWander = Direction.getRandom();
      Routines.turnThenMove(getAgent(), randomWander, true, false);

      // Apply this wander step to the counter.
      wanderChain--;

    }

  }


  @Override
  public void onActorTurnComplete() {

    if (getActor().isFreeToAct()) {
      wander();
    }

  }

  @Override
  public void onActionExecuted(Action action) {

    // If one of our movements fails, or sometimes even if it succeeds, run the main routine to
    // start a new wander step.
    if (action.hasFlag(ActionFlag.FAILED)
        || (action.hasFlag(ActionFlag.SUCCEEDED) && Game.RANDOM.nextInt(10) < 4)) {
      wander();
    }

    // Perform a sensory scan after each of our movements.
    Routines.performSensoryScan(getAgent());

  }

  @Override
  public void onVictimized(Actor attacker) {

    // If we are attacked, either fight or flee.
    Routines.evaluateNewAggressor(getAgent(), attacker);

  }

}