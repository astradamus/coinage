package controller.ai;

import actor.Actor;
import controller.action.Turning;
import game.Direction;
import game.Game;

/**
 * This behavior will make the agent hold position in one spot and, on occasion, turn to look
 * around.<br><br>
 *
 * The agent will perform a sensory scan every few turns at a statically defined interval, but will
 * otherwise do nothing for the duration of the behavior.
 */
public class Ai_Idle extends Behavior {

  private static final int SENSORY_SCAN_INTERVAL = 5;

  private static final int IDLE_DURATION_BASE  = 75;
  private static final int IDLE_DURATION_RANGE = 75;


  private int idleTimeRemaining;

  public Ai_Idle(AiActorAgent agent) {
    super(agent);
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
        Routines.performSensoryScan(getAgent());
      }

      // On occasion, turn one grade to the left or the right.
      if (getActor().isFreeToAct() && Game.RANDOM.nextInt(100) < 1) {

        Direction turnTo = getActor().getFacing();

        if (Game.RANDOM.nextBoolean()) {
          turnTo = turnTo.getLeftNeighbor();
        }
        else {
          turnTo = turnTo.getRightNeighbor();
        }

        getAgent().attemptAction(new Turning(getActor(), turnTo));

      }

    }

  }


  @Override
  public void onActorTurnComplete() {

    // Run the main routine at the end of every update.
    idle();

  }

  @Override
  public void onVictimized(Actor attacker) {

    // If we are attacked, either fight or flee.
    Routines.evaluateNewAggressor(getAgent(), attacker);

  }

}