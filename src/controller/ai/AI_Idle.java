package controller.ai;

import controller.ActorController;
import controller.action.Turning;
import game.Direction;
import game.Game;

/**
 *
 */
public class AI_Idle extends AIBehavior {

  public static final int SENSORY_SCAN_INTERVAL = 5;

  public static final int IDLE_DURATION_BASE  = 75;
  public static final int IDLE_DURATION_RANGE = 75;

  private int idleTimeRemaining;

  public AI_Idle(AIController idler) {
    super(idler);
    idleTimeRemaining = IDLE_DURATION_BASE + Game.RANDOM.nextInt(IDLE_DURATION_RANGE);

    idle();
  }

  private void idle() {
    if (idleTimeRemaining <= 0) {
      markComplete();
      return;
    }


    // Scans every once in a while. This delay can allow you to get a little closer to actors who
    // don't see you coming--which is actually a good thing. It feels quite like sneaking up on
    // them, or that reaction time is reasonably delayed. If your reflex is 17 you can zip right
    // up before they even notice you.
    if (idleTimeRemaining % SENSORY_SCAN_INTERVAL == 0) {
      AIRoutines.performSensoryScan(getPuppet());
    }



    idleTimeRemaining--;

    if (getPuppet().isFreeToAct() && Game.RANDOM.nextInt(100) < 1) {

      Direction turnTo = getPuppet().getActor().getFacing();
      turnTo = Game.RANDOM.nextBoolean() ? turnTo.getLeftNeighbor() : turnTo.getRightNeighbor();

      getPuppet().attemptAction(new Turning(getPuppet(), turnTo));
    }
  }


  @Override
  public void onActorTurnComplete() {
    idle();
  }


  @Override
  public void onVictimized(ActorController attacker) {
    AIRoutines.fightOrFlee(getPuppet(), attacker);
  }

}