package controller;

import actor.Actor;
import controller.action.Action;

/**
 *
 */
public interface ActorObserver {

  default void disconnectActorObserver() {
    throw new IllegalStateException("Does not implement disconnect.");
  }

  default void onActorTurnComplete() { }

  default void onActionExecuted(Action action) { }

  default void onVictimized(Actor attacker) { }

}