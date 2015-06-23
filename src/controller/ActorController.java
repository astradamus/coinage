package controller;

import actor.Actor;
import actor.attribute.Attribute;
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
public abstract class ActorController implements Controller, ActorObserver {

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

      final Action executing = action;
      final boolean performedSuccessfully = executing.perform();
      actionDelayClock.addBeatsToCoolDown(action.calcDelayToRecover());

      if (performedSuccessfully) {

        if (executing.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {
          Area from = executing.getOrigin().area;
          Area to = actor.getCoordinate().area;
          Game.getActiveControllers().moveController(this, from, to);
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

    onUpdateFinished();

  }

  @Override
  public Area getLocality() {
    return actor.getCoordinate().area;
  }

  @Override
  public Integer getRolledInitiative() {
    return Game.RANDOM.nextInt(actor.readAttributeLevel(Attribute.REFLEX).ordinal());
  }

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