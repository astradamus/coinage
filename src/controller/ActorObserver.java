package controller;

import controller.action.Action;

/**
 *
 */
public interface ActorObserver {

  default void onActionExecuted(Action action) { }

  default void onActorTurnComplete() { }

  default void onVictimized(ActorController attacker) { }


}
