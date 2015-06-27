package controller.ai;

import controller.ActorController;
import controller.action.Action;
import controller.action.ActionFlag;
import controller.action.Moving;
import controller.action.TurnThenMove;
import game.Direction;
import game.Game;

/**
 *
 */
public class AI_Wander extends AIBehavior {

  public static final int WANDER_CHAIN_MAX_LENGTH = 3;

  private int wanderChain;

  public AI_Wander(AIController wanderer) {
    super(wanderer);

    wanderChain = Game.RANDOM.nextInt(WANDER_CHAIN_MAX_LENGTH) + 1;
    wander();
  }

  private void wander() {

    if (wanderChain <= 0) {
      markComplete();
      return;
    }

    wanderChain--;

    Direction direction = Direction.getRandom();

    Action next;

    ActorController wanderer = getPuppet();

    if (wanderer.getActor().getFacing() != direction) {
      next = new TurnThenMove(wanderer, direction, true);
    } else {
      next = new Moving(wanderer, direction, true);
    }

    wanderer.attemptAction(next);

  }

  @Override
  public void onActionExecuted(Action action) {
    if (action.hasFlag(ActionFlag.FAILED)
        || (action.hasFlag(ActionFlag.SUCCEEDED) && Game.RANDOM.nextInt(10) < 4)) {
      wander();
    }
  }

  @Override
  public void onActorTurnComplete() {
    AIRoutines.performSensoryScan(getPuppet());
  }

  @Override
  public void onVictimized(ActorController attacker) {
    AIRoutines.fightOrFlee(getPuppet(), attacker);
  }

}