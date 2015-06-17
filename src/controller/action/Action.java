package controller.action;

import actor.Actor;
import world.Coordinate;

import java.awt.*;
import java.util.EnumSet;

/**
 *
 */
public abstract class Action {

  private final Actor actor;
  private final Coordinate actorAt;

  private final Coordinate target;

  private final EnumSet<ActionFlag> flags = EnumSet.noneOf(ActionFlag.class);

  protected Action(Actor actor, Coordinate target) {
    this.actor = actor;
    this.actorAt = actor.getCoordinate();

    this.target = target;
  }


  public int calcDelayToPerform() {
    return 0;
  }

  protected int calcDelayToRecover() {
    return 0;
  }

  protected abstract boolean validate();
  protected abstract void apply();

  public Color getIndicatorColor() {
    return null;
  }

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

    getActor().addBeatsToActionDelay(calcDelayToRecover());
    return valid;

  }


  public void doNotRepeat() {
    addFlag(ActionFlag.DO_NOT_REPEAT);
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

  public final Coordinate getTarget() {
    return target;
  }


}
