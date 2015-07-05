package controller.action;

import actor.Actor;
import game.Game;
import world.Coordinate;
import world.World;

import java.awt.Color;
import java.util.EnumSet;

/**
 * Actors perform actions to manifest changes in themselves or the world around them. Actions
 * must be instantiable by actors (or rather, their controllers), meaning they cannot require
 * information actors do not have to construct. However, actions also cannot be executed by
 * actors--because the details of their execution generally requires information actors cannot
 * get, they must be passed upwards and executed at a higher level that can supply access to the
 * current game's {@code World}.
 */
public abstract class Action {

  private final Actor actor;
  private final Coordinate origin;
  private final Coordinate target;
  private final EnumSet<ActionFlag> flags;

  protected Action(Actor actor, Coordinate targetWhere) {
    this.actor = actor;
    this.origin = actor.getCoordinate();
    this.target = targetWhere;
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
  public final boolean perform(World world) {

    if (hasFlag(ActionFlag.SUCCEEDED) || hasFlag(ActionFlag.FAILED)) {
      throw new IllegalStateException("Action has already been performed.");
    }

    final boolean valid = validate(world);

    if (valid) {
      addFlag(ActionFlag.SUCCEEDED);
      apply(world);
    } else {
      addFlag(ActionFlag.FAILED);
    }

    return valid;

  }

  /**
   * Should return {@code true} if this action's effects are legal to apply immediately.
   */
  protected abstract boolean validate(World world);

  /**
   * Called after the action has been validated, and applies all effects.
   */
  protected abstract void apply(World world);

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

  public Action doNotRepeat() {
    addFlag(ActionFlag.DO_NOT_REPEAT);
    return this;
  }


  protected final Actor getActor() {
    return actor;
  }

  public final Coordinate getOrigin() {
    return origin;
  }

  public final Coordinate getTarget() {
    return target;
  }

  protected final boolean getPlayerIsActor() {
    return getActor() == Game.getActivePlayerActor();
  }


}