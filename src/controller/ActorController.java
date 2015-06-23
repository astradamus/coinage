package controller;

import actor.Actor;
import controller.action.Action;
import controller.action.ActionFlag;
import game.Game;
import game.physical.PhysicalFlag;
import world.Area;
import world.Coordinate;

import java.awt.Color;

/**
 *
 */
public abstract class ActorController implements Controller {

  private final Actor actor;
  private final ActionDelayClock actionDelayClock;

  private Action action;


  public ActorController(Actor actor) {
    if (actor == null) {
      throw new IllegalArgumentException("Actor cannot be null.");
    }

    this.actor = actor;
    this.actionDelayClock = new ActionDelayClock();
  }

  protected final Action getCurrentAction() {
    return action;
  }

  public final void cancelAction() {
    actionDelayClock.cancelWarmUp();
    action = null;
  }


  public final void attemptAction(Action action) {
    actionDelayClock.cancelWarmUp();
    actionDelayClock.addBeatsToWarmUp(action.calcDelayToPerform());
    this.action = action;
  }

  @Override
  public final void onUpdate() {

    if (actor.hasFlag(PhysicalFlag.DEAD)) {
      Game.getActiveControllers().removeController(this);
      return;
    }

    if (!actionDelayClock.isReady()) {
      actionDelayClock.decrementClock();
    }
    else if (action != null) {

      Action executing = action;

      if (executing.perform()) {
        actionDelayClock.addBeatsToCoolDown(action.calcDelayToRecover());

        if (executing.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {
          Area from = executing.getOrigin().area;
          Area to = actor.getCoordinate().area;
          Game.getActiveControllers().moveController(this,from,to);
        }

        // If this action can repeat upon succeeding, attempt to do so.
        Action repeat = executing.attemptRepeat();
        if (repeat != null) {
          attemptAction(repeat);
        }

      }

      // If we haven't attempted a new action yet, start idling.
      if (action == executing) {
        action = null;
      }

      onActionExecuted(executing);

    }

    onUpdateProcessed();

  }

  @Override
  public Area getLocality() {
    return actor.getCoordinate().area;
  }

  @Override
  public int getRolledInitiative() {
    return 0;
  }

  /**
   * Subclasses of ActorController should override this method to hook into successful movement.
   */
  public void onActionExecuted(Action action) {

  }

  /**
   * Subclasses of ActorController should override this method to hook into game updates. This
   * function is run at the end of this ActorController's onUpdate() call, and should be used to
   * setup the actor for FUTURE updates, rather than the current one.
   */
  protected void onUpdateProcessed() { }

  public Actor getActor() {
    return actor;
  }

  public ActionDelayClock getActionDelayClock() {
    return actionDelayClock;
  }

  public boolean isFreeToAct() {
    return action == null && actionDelayClock.isReady();
  }

  public Color getActionIndicatorColor() {
    if (action != null) {
      return action.getIndicatorColor();
    } else {
      return null;
    }
  }

  public Coordinate getActionTarget() {
    if (action != null) {
      return action.getTarget();
    } else {
      return null;
    }
  }

}