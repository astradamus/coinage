package controller.action;

import actor.Actor;
import game.Direction;

/**
 *
 */
public class Turning extends Action {

  private final boolean attemptMoveAfterTurn;

  public Turning(Actor actor, Direction direction, boolean attemptMoveAfterTurn) {
    super(actor, direction);
    this.attemptMoveAfterTurn = attemptMoveAfterTurn;
  }

  @Override
  public int calcBeatsToPerform() {
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
      if (attemptMoveAfterTurn) {
        return new Moving(getActor(), getDirection(), false);
      } else {
        return null;
      }
    } else {
      return new Turning(getActor(), getDirection(), attemptMoveAfterTurn);
    }
  }

}
