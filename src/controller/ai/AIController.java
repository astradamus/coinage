package controller.ai;

import actor.Actor;
import controller.ActorController;
import controller.action.Action;
import game.Game;

/**
 *
 */
public class AIController extends ActorController {

  private AIBehavior currentBehavior = null;

  public AIController(Actor actor) {
    super(actor);
  }


  void exhibitBehavior(AIBehavior behavior) {
    currentBehavior = behavior;
  }


  @Override
  public void onActionExecuted(Action action) {
    if (currentBehavior != null) {
      currentBehavior.onActionExecuted(action);
    }
  }

  @Override
  public void onActorTurnComplete() {

    if (currentBehavior == null || currentBehavior.getIsComplete()) {

      if (Game.RANDOM.nextInt(10) < 2) {
        currentBehavior = new AI_Wander(this);
      } else {
        currentBehavior = new AI_Idle(this);
      }

    }

    currentBehavior.onActorTurnComplete();

  }

  @Override
  public void onVictimized(ActorController attacker) {

    if (currentBehavior == null) {
      currentBehavior = new AI_Retreat(this, attacker);
    } else {
      currentBehavior.onVictimized(attacker);
    }

  }

}