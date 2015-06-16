package controller.action;

import actor.Actor;
import game.Direction;

/**
 *
 */
public class Turning extends Action {

  public Turning(Actor actor, Direction direction) {
    super(actor, direction);
  }

  @Override
  protected int calcBeatsToPerform() {
    return 1;
  }

  @Override
  protected boolean validate() {
    return true;
  }

  @Override
  protected void apply() {

    Direction actorFacing = getActor().getFacing();

    int difference = actorFacing.ordinal() - getDirection().ordinal();

    if ((difference > 0 && difference <= 4) || difference < -4) {
      getActor().setFacing(actorFacing.getLeftNeighbor());
    } else {
      getActor().setFacing(actorFacing.getRightNeighbor());
    }

  }

  @Override
  public Action attemptRepeat() {
    if (getActor().getFacing() == getDirection()) {
      return null;
    } else {
      return new Turning(getActor(),getDirection());
    }
  }

}
