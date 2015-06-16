package controller.action;

import actor.Actor;
import game.Direction;
import world.Coordinate;

import java.util.EnumSet;

/**
 *
 */
public abstract class Action {

  private final Actor actor;
  private final Coordinate actorAt;

  private final ActionTarget target;
  private final Direction direction;

  private final EnumSet<ActionFlag> flags = EnumSet.noneOf(ActionFlag.class);

  protected Action(Actor actor, ActionTarget target) {
    this.actor = actor;
    this.actorAt = actor.getCoordinate();

    this.direction = null;
    this.target = target;
  }

  protected Action(Actor actor, Direction direction) {
    this.actor = actor;
    this.actorAt = actor.getCoordinate();

    this.direction = direction;
    this.target = null;
  }


  public int calcBeatsToPerform() {
    return 0;
  }

  protected int calcBeatsToRecover() {
    return 0;
  }

  protected abstract boolean validate();
  protected abstract void apply();


  public final boolean execute() {

    if (hasFlag(ActionFlag.SUCCEEDED) || hasFlag(ActionFlag.FAILED)) {
      throw new IllegalStateException("Action has already been executed.");
    }

    boolean valid = validate();

    if (valid) {
      addFlag(ActionFlag.SUCCEEDED);
      apply();
    } else {
      addFlag(ActionFlag.FAILED);
    }

    getActor().addBeatsToActionDelay(calcBeatsToRecover());
    return valid;

  }

  public final boolean hasFlag(ActionFlag flag) {
    return flags.contains(flag);
  }

  public Action attemptRepeat() {
    return null;
  }

  protected final void addFlag(ActionFlag flag) {
    flags.add(flag);
  }

  protected final Actor getActor() {
    return actor;
  }

  public final Coordinate getActorAt() {
    return actorAt;
  }

  protected final ActionTarget getActionTarget() {
    return target;
  }

  protected final Direction getDirection() {
    return direction;
  }

}
