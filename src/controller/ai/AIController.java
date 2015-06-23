package controller.ai;

    import actor.Actor;
    import controller.ActorController;
    import controller.action.Action;

/**
 *
 */
public class AIController extends ActorController {

  private AI_Behavior currentBehavior = null;

  public AIController(Actor actor) {
    super(actor);
  }

  @Override
  public void onActionExecuted(Action action) {
    if (currentBehavior != null) {
      currentBehavior.onActionExecuted(action);
    }
  }

  @Override
  public void onActorTurnComplete() {

    if (currentBehavior == null || currentBehavior.isComplete()) {
      currentBehavior = new AI_Wander(this);
    }

    currentBehavior.onActorTurnComplete();

  }

  @Override
  public void onVictimized(ActorController attacker) {
    if (currentBehavior != null) {
      currentBehavior.onVictimized(attacker);
    }
  }

}
