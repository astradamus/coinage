package controller;

import actor.Actor;
import actor.action.Action;

/**
 *
 */
public interface ActorObserver {

  default void onActionExecuted(Action action) { }

  default void onActorTurnComplete() { }

  default void onVictimized(Actor attacker) { }

  default void disconnectActorObserver() {
    throw new IllegalStateException("Does not implement disconnect.");
  }
}