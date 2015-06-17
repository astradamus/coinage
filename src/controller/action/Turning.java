package controller.action;

import actor.Actor;
import game.Direction;
import game.Game;

/**
 *
 */
public class Turning extends Action {

  private final Direction direction;
  private final boolean attemptMoveAfterTurn;

  public Turning(Actor actor, Direction direction, boolean attemptMoveAfterTurn) {
    super(actor,
        Game.getActiveWorld().offsetCoordinateBySquares(
            actor.getCoordinate(),direction.relativeX,direction.relativeY));

    this.direction = direction;
    this.attemptMoveAfterTurn = attemptMoveAfterTurn;

  }

  @Override
  protected boolean validate() {
    return true;
  }

  @Override
  protected void apply() {

    Direction actorFacing = getActor().getFacing();

    int difference = actorFacing.ordinal() - direction.ordinal();

    if ((difference > 0 && difference <= 4) || difference < -4) {
      getActor().setFacing(actorFacing.getLeftNeighbor());
    } else {
      getActor().setFacing(actorFacing.getRightNeighbor());
    }

  }

  @Override
  public Action attemptRepeat() {

    // todo: clean up this nightmare. The point is to have single-taps of directions we're not
    // facing follow through with turning to that direction AND moving one step, but no more.

    boolean repeatCancelled = false;

    if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
      repeatCancelled = true;
    }

    if (getActor().getFacing() == direction) {
      if (attemptMoveAfterTurn) {
        Moving moving = new Moving(getActor(), direction, false);
        if (repeatCancelled) {
          moving.doNotRepeat();
        }
        return moving;
      } else {
        return null;
      }
    } else {
      Turning turning = new Turning(getActor(), direction, attemptMoveAfterTurn);
      if (repeatCancelled) {
        turning.doNotRepeat();
      }
      return turning;
    }
  }

}
