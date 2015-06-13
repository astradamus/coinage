package controller;

import controller.action.Action;
import actor.Actor;
import controller.action.ActionFlag;
import game.Game;

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

  protected final Action getCurrentAction() {
    return action;
  }


  public final void attemptAction(Action action) {
    this.action = action;
  }

  @Override
  public final void onUpdate() {

    if (actor.isDead()) {
      Game.getActiveControllers().unregister(this);
      return;
    }

    if (actor.getIsReadyThisBeat() && action != null) {

      Action executing = action;

      if (executing.execute()) {

        // If this action should repeat upon succeeding, attempt to do so.
        if (executing.hasFlag(ActionFlag.REPEAT_ON_SUCCESS)) {
          attemptAction(executing.attemptRepeat());
        }

      }

      // If we haven't switched to a new action yet, start idling.
      if (action == executing) {
        action = null;
      }

      onActionExecuted(executing);

    }

    onUpdateProcessed();

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

}