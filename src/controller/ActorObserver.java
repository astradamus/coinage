package controller;

import actor.Actor;
import controller.action.Action;

/**
 *
 */
public interface ActorObserver {

  default void disconnectObserver() { }

  default void onActorTurnComplete() { }

  default void onActionExecuted(Action action) { }

  default void onVictimized(Actor attacker) { }

}