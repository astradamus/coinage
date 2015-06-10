package controller.action;

import actor.Actor;
import game.Direction;
import game.Game;
import world.Coordinate;

/**
 *
 */
public class Moving extends Action {

  public Moving(Actor actor, Direction direction) {
    super(actor, direction);
    addFlag(ActionFlag.REPEAT_ON_SUCCESS);
  }

  @Override
  protected int calcBeatsToPerform() {
    return 3;
  }

  @Override
  protected boolean validate() {

    Coordinate newCoordinate = Game.getActiveWorld().offsetCoordinateBySquares(getActorAt(),
        getDirection().relativeX, getDirection().relativeY);

    return getActor().attemptMoveTo(newCoordinate);

  }

  @Override
  protected void apply() {

    getActor().setFacing(getDirection());

    if (getActorAt().area != getActor().getCoordinate().area) {
      addFlag(ActionFlag.ACTOR_CHANGED_AREA);
    }

  }

  @Override
  public Moving attemptRepeat() {

    // Important! We must get the actor's current (NEW) coordinate, not the one from this action.
    return new Moving(getActor(), getDirection());

  }

}
