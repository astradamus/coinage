package controller.ai;

import controller.ActorObserver;

/**
 *
 */
public abstract class AIBehavior implements ActorObserver {

  private final AIController puppet;

  private boolean complete;

  protected AIBehavior(AIController puppet) {
    this.puppet = puppet;
  }

  protected final AIController getPuppet() {
    return puppet;
  }

  protected final void markComplete() {
    complete = true;
  }

  final boolean getIsComplete() {
    return complete;
  }

}
