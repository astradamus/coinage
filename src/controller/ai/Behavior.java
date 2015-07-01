package controller.ai;

import actor.Actor;
import controller.ActorObserver;

/**
 * Provides a set of actions and interpretations to achieve some kind of result or apparent
 * intelligence with a non-player-controlled actor.
 */
public abstract class Behavior implements ActorObserver {

  private final AiActorAgent agent;

  private boolean complete;

  protected Behavior(AiActorAgent agent) {
    this.agent = agent;
  }

  /**
   * @return A string to print to the event log, or null if nothing should be printed. Will be
   * printed in Event.OTHER_ACTOR_ACTIONS color.
   */
  protected String getOnExhibitLogMessage() {
    return null;
  }


  /**
   * Called when the behavior is first adopted by the agent. Override if the behavior needs some
   * kind of initialization at this point.
   */
  protected void onExhibit() {

  }


  /**
   * @return The AiActorAgent exhibiting this behavior.
   */
  protected final AiActorAgent getAgent() {
    return agent;
  }

  /**
   * @return The actor controlled by the AiActorAgent exhibiting this behavior.
   */
  protected final Actor getActor() {
    return agent.getActor();
  }

  /**
   * Mark this behavior as having completed its goals. Allows the AiActorAgent exhibiting this
   * behavior to discard it and return to an idle state.
   */
  protected final void markComplete() {
    complete = true;
  }

  /**
   * Check if this behavior has completed its goals. Allows the AiActorAgent exhibiting this
   * behavior to discard it and return to an idle state.
   */
  final boolean getIsComplete() {
    return complete;
  }

}