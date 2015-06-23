package controller.ai;

import controller.ActorController;
import controller.ActorObserver;

/**
 *
 */
public abstract class AI_Behavior implements ActorObserver {

  private final ActorController puppet;

  private boolean complete;

  protected AI_Behavior(ActorController puppet) {
    this.puppet = puppet;
  }


  protected final void markComplete() {
    complete = true;
  }

  final boolean isComplete() {
    return complete;
  }

}
