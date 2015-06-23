package controller.action;

import actor.Actor;
import world.Coordinate;

import java.awt.*;
import java.util.EnumSet;

/**
 * Actors perform actions to manifest changes in themselves or the world around them.
 */
public abstract class Action {


  private final Actor performer;
  private final Coordinate origin;
  private final Coordinate target;
  private final EnumSet<ActionFlag> flags;

  protected Action(Actor performer, Coordinate target) {
    this.performer = performer;
    this.origin = performer.getCoordinate();
    this.target = target;
    this.flags = EnumSet.noneOf(ActionFlag.class);
  }

  /**
   * @return The action overlay indicator color, or null if this action should not have one.
   */
  public Color getIndicatorColor() {
    return null;
  }





  /**
   * @return The number of updates that the actor must delay before the action can be performed.
   */
  public int calcDelayToPerform() {
    return 0;
  }

  /**
   * @return The number of updates that the actor must delay after the action has been performed.
   */
  public int calcDelayToRecover() {
    return 0;
  }



  /**
   * Begins executing this action. If {@code validate()} returns {@code true}, the action is
   * legal at the time of execution and {@code apply()} will immediately be called to implement
   * the action's effects. The action gains either the {@code SUCCEEDED} or {@code FAILED} flag,
   * based on this validation. Finally, succeed or fail, {@code calcDelayToRecover()} is called
   * and added to the performing actor's action delay.
   *
   * @throws IllegalStateException If this action has already been performed.
   */
  public final boolean perform() {

    if (hasFlag(ActionFlag.SUCCEEDED) || hasFlag(ActionFlag.FAILED)) {
      throw new IllegalStateException("Action has already been performed.");
    }

    final boolean valid = validate();

    if (valid) {
      addFlag(ActionFlag.SUCCEEDED);
      apply();
    } else {
      addFlag(ActionFlag.FAILED);
    }

    return valid;

  }

  /**
   * Should return {@code true} if this action's effects are legal to apply immediately.
   */
  protected abstract boolean validate();

  /**
   * Called after the action has been validated, and applies all effects.
   */
  protected abstract void apply();

  /**
   * Should return a new action to be performed by this actor immediately following this action, or
   * {@code null} if no such action is appropriate.
   */
  public Action attemptRepeat() {
    return null;
  }



  protected final void addFlag(ActionFlag flag) {
    flags.add(flag);
  }

  public final boolean hasFlag(ActionFlag flag) {
    return flags.contains(flag);
  }

  public void doNotRepeat() {
    addFlag(ActionFlag.DO_NOT_REPEAT);
  }



  protected final Actor getPerformer() {
    return performer;
  }

  public final Coordinate getOrigin() {
    return origin;
  }

  public final Coordinate getTarget() {
    return target;
  }


}
