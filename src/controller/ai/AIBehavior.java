package controller.ai;

import controller.ActorObserver;

/**
 * Provides a set of actions and interpretations to achieve some kind of result or apparent
 * intelligence with a non-player-controlled actor.
 */
public abstract class AIBehavior implements ActorObserver {

  private final AIController puppet;

  private boolean complete;

  protected AIBehavior(AIController puppet) {
    this.puppet = puppet;
  }

  /**
   * @return A string to print to the event log, or null if nothing should be printed. Will be
   * printed in Event.OTHER_ACTOR_ACTIONS color.
   */
  protected String getOnExhibitLogMessage() {
    return null;
  }


  /**
   * Called when the behavior is first adopted by the puppet. Override if the behavior needs some
   * kind of initialization at this point.
   */
  protected void onExhibit() {

  }


  /**
   * @return The AIController exhibiting this behavior.
   */
  protected final AIController getPuppet() {
    return puppet;
  }

  /**
   * Mark this behavior as having completed its goals. Allows the AIController exhibiting this
   * behavior to discard it and return to an idle state.
   */
  protected final void markComplete() {
    complete = true;
  }

  /**
   * Check if this behavior has completed its goals. Allows the AIController exhibiting this
   * behavior to discard it and return to an idle state.
   */
  final boolean getIsComplete() {
    return complete;
  }

}