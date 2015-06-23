package controller;

import controller.action.Action;

/**
 *
 */
public interface ActorObserver {

  default void onActionExecuted(Action action) { }

  default void onVictimized(ActorController attacker) { }

  default void onUpdateFinished() { }

}
