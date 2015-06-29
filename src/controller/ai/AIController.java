package controller.ai;

import actor.Actor;
import controller.ActorController;
import controller.action.Action;
import game.display.Event;
import game.display.EventLog;
import game.physical.PhysicalFlag;

/**
 * This actor controller uses modular {@code AIBehavior} packages to give non-player-characters
 * behavior. It hands off many of its method calls to whichever behavior it is currently
 * exhibiting, so that the interpretation of that call can vary situationally. One package can
 * freely call for the controller to switch to another. All packages can flag themselves with
 * {@code markComplete()} to tell the controller it can safely return to an idle state.
 */
public class AIController extends ActorController {

  private AIBehavior currentBehavior;

  public AIController(Actor actor) {
    super(actor);
    currentBehavior = null;
  }

  /**
   * Changes the current behavior package to the one specified and starts the routine. If there
   * is a relevant event log message to be printed, do so.
   */
  void exhibitBehavior(AIBehavior behavior) {

    currentBehavior = behavior;

    final String onExhibitLogMessage = currentBehavior.getOnExhibitLogMessage();

    // We must check if the actor is dead because otherwise ghosts will log weird messages.
    if (onExhibitLogMessage != null&& !getActor().hasFlag(PhysicalFlag.DEAD)) {
      EventLog.registerEvent(Event.OTHER_ACTOR_ACTIONS, onExhibitLogMessage);
    }

    currentBehavior.onExhibit();

  }


  @Override
  public void onActionExecuted(Action action) {

    // Pass the call to our current behavior, if we have one.
    if (currentBehavior != null) {
      currentBehavior.onActionExecuted(action);
    }

  }

  @Override
  public void onActorTurnComplete() {

    // If we have no behavior or our current behavior is finished, enter a simple idling state.
    if (currentBehavior == null || currentBehavior.getIsComplete()) {
      currentBehavior = new AI_Idle(this);
    }

    // Pass the call to our current behavior.
    currentBehavior.onActorTurnComplete();

  }

  @Override
  public void onVictimized(ActorController attacker) {

    // Pass the call to our current behavior, if we have one.
    if (currentBehavior != null) {
      currentBehavior.onVictimized(attacker);
    }

  }

}