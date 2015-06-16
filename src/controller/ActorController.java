package controller;

import controller.action.Action;
import actor.Actor;
import controller.action.ActionFlag;
import game.Game;
import world.Area;

/**
 *
 */
public abstract class ActorController implements Controller {

  private final Actor actor;

  private Action action;


  public ActorController(Actor actor) {
    if (actor == null) {
      throw new IllegalArgumentException("Actor cannot be null.");
    }

    this.actor = actor;
  }

  public final Action getCurrentAction() {
    return action;
  }


  public final void attemptAction(Action action) {
    if (action != null) {
      actor.addBeatsToActionDelay(action.calcBeatsToPerform());
    }

    this.action = action;
  }

  @Override
  public final void onUpdate() {

    if (actor.isDead()) {
      Game.getActiveControllers().removeController(this);
      return;
    }

    if (!actor.isReadyToAct()) {
      actor.decrementActionDelay();
    }
    else if (action != null) {

      Action executing = action;

      if (executing.execute()) {


        if (executing.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {
          Area from = executing.getActorAt().area;
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

  public boolean isFreeToAct() {
    return action == null && actor.isReadyToAct();
  }

}