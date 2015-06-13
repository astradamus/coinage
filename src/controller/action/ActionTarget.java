package controller.action;

import game.Physical;
import world.Coordinate;

/**
 *
 */
public class ActionTarget<T extends Physical> {

  private final T target;
  private final Coordinate targetAt;

  public ActionTarget(T target, Coordinate targetAt) {
    this.target = target;
    this.targetAt = targetAt;
  }

  public T getTarget() {
    return target;
  }

  public Coordinate getTargetAt() {
    return targetAt;
  }

}
