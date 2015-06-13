package controller.action;

import game.Physical;
import world.Coordinate;

/**
 *
 */
public class ActionTarget {

  private final Physical target;
  private final Coordinate targetAt;

  public ActionTarget(Physical target, Coordinate targetAt) {
    this.target = target;
    this.targetAt = targetAt;
  }

  public Physical getTarget() {
    return target;
  }

  public Coordinate getTargetAt() {
    return targetAt;
  }

}
