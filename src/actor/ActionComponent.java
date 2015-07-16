package actor;

import controller.action.Action;
import controller.action.ActionFlag;
import game.Executor;
import world.Coordinate;

import java.awt.Color;

public class ActionComponent {

  private final Actor actor;

  private final ActionTimer actionTimer;

  private Action action;


  public ActionComponent(Actor actor) {
    this.actor = actor;
    actionTimer = new ActionTimer();
  }


  /**
   * @return {@code true} if this actor's warm-up and cool-down timers are cleared and the actor
   * does not currently have an action queued.
   */
  public final boolean isFreeToAct() {
    return action == null && actionTimer.isReady();
  }


  /**
   * Queues an action to be performed, adding that action's warm-up time to the timer. If there was
   * already an action queued, it is replaced, and the warm-up time for that action is cleared
   * before the new time is added. In other words, there is no penalty for switching actions, except
   * that you lose the beats you spent warming up the replaced action.
   */
  public final void startAction(Action action) {
    actionTimer.cancelWarmUp();
    actionTimer.addBeatsToWarmUp(action.calcDelayToPerform());
    this.action = action;
  }


  /**
   * Notifies the current action that it should not repeat, if it is able.
   */
  public void doNotRepeatAction() {
    if (action != null) {
      action.doNotRepeat();
    }
  }


  /**
   * Called every update when it is this actor's turn to act. If the actor has any warm-up or
   * cool-down time, then this update is spent towards that time. Otherwise, if there is an action
   * queued, the actor will perform it. If the action is successful, an attempt will be made to
   * repeat the action. Successful or not, the actor's actorObserver will be notified and passed the
   * completed action. <p> <p>Additionally, regardless of whether an action is performed, the
   * actor's actorObserver will be notified at the very end of this actor's turn.</p>
   *
   * @param executor Supplied by {@code GameControllers}, allowing this actor to pass an action
   *                 upwards for execution, as actors lack the scope/authority to execute their own
   *                 actions.
   */
  public final void onUpdate(Executor executor) {

    if (!actionTimer.isReady()) {
      actionTimer.decrementClock();
    }
    else if (action != null) {

      final Action executing = action;
      final boolean performedSuccessfully = executor.executeAction(executing);
      actionTimer.addBeatsToCoolDown(action.calcDelayToRecover());

      if (performedSuccessfully) {

        // If this action can repeat upon succeeding, attempt to do so.
        Action repeat = executing.attemptRepeat();
        if (repeat != null) {

          // Copy repeating flag(s).
          if (repeat.hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
            repeat.playerIsActor();
          }

          startAction(repeat);
        }
      }

      // If we haven't attempted a new action yet, start idling.
      if (action == executing) {
        action = null;
      }

      actor.getActorObserver().onActionExecuted(executing);
    }

    actor.getActorObserver().onActorTurnComplete();
  }


  /**
   * @return What color the action indicator should be for this actor's current action, or null if
   * there is none.
   */
  public Color getActionIndicatorColor() {
    if (action != null) {
      return action.getIndicatorColor();
    }
    else {
      return null;
    }
  }


  /**
   * @return The total amount of beats that must be spent before the actor can perform the currently
   * queued action.
   */
  public int getTotalActionDelay() {
    return actionTimer.getTotalDelay();
  }


  /**
   * @return The coordinate that is the target of this actor's current action, or null if there is
   * none.
   */
  public Coordinate getActionTarget() {
    if (action != null) {
      return action.getTarget();
    }
    else {
      return null;
    }
  }
}