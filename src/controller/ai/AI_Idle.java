package controller.ai;

import controller.ActorController;
import controller.action.Turning;
import game.Direction;
import game.Game;

/**
 * This behavior will make the puppet hold position in one spot and, on occasion, turn to look
 * around.<br><br>
 *
 * The puppet will perform a sensory scan every few turns at a statically defined interval, but will
 * otherwise do nothing for the duration of the behavior.
 */
public class AI_Idle extends AIBehavior {

  public static final int SENSORY_SCAN_INTERVAL = 5;

  public static final int IDLE_DURATION_BASE  = 75;
  public static final int IDLE_DURATION_RANGE = 75;


  private int idleTimeRemaining;

  public AI_Idle(AIController idler) {
    super(idler);
    idleTimeRemaining = IDLE_DURATION_BASE + Game.RANDOM.nextInt(IDLE_DURATION_RANGE);
  }

  @Override
  protected void onExhibit() {
    idle();
  }

  private void idle() {

    // If the timer is up, we can stop idling.
    if (idleTimeRemaining <= 0) {
      markComplete();
    }

    else {

      // Advance the timer.
      idleTimeRemaining--;

      // Perform a sensory scan every few updates.
      if (idleTimeRemaining % SENSORY_SCAN_INTERVAL == 0) {
        AIRoutines.performSensoryScan(getPuppet());
      }

      // On occasion, turn lazily (one grade) to the left or the right.
      if (getPuppet().isFreeToAct() && Game.RANDOM.nextInt(100) < 1) {

        Direction turnTo = getPuppet().getActor().getFacing();

        if (Game.RANDOM.nextBoolean()) {
          turnTo = turnTo.getLeftNeighbor();
        }
        else {
          turnTo = turnTo.getRightNeighbor();
        }

        getPuppet().attemptAction(new Turning(getPuppet(), turnTo));

      }

    }

  }


  @Override
  public void onActorTurnComplete() {

    // Run the main routine at the end of every update.
    idle();

  }

  @Override
  public void onVictimized(ActorController attacker) {

    // If we are attacked, either fight or flee.
    AIRoutines.evaluateNewAggressor(getPuppet(), attacker);

  }

}