package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import game.Direction;
import game.Game;

import java.awt.*;

/**
 *
 */
public class Moving extends Action {

  private final Direction direction;
  private final boolean isStrafing;

  public Moving(Actor actor, Direction direction, boolean isStrafing) {
    super(actor,
        Game.getActiveWorld().offsetCoordinateBySquares(
            actor.getCoordinate(),direction.relativeX,direction.relativeY));
    this.direction = direction;
    this.isStrafing = isStrafing;
  }


  /*
   * At BASELINE_RANK reflex, an actor will suffer a BEATS_AT_BASELINE recovery. For every
   * DISTANCE_FROM_BASELINE_DIVISOR ranks above or below BASELINE_RANK, the actor suffers one
   * less or one more beat of recovery, respectively.
   */
  private final static int BASELINE_RANK = 5;
  private final static int BEATS_AT_BASELINE = 4;
  private final static int DISTANCE_FROM_BASELINE_DIVISOR = 2;

  @Override
  public int calcDelayToPerform() {

    int actorReflex = getActor().readAttributeLevel(Attribute.REFLEX).ordinal();

    int distanceFromBaseline = actorReflex - BEATS_AT_BASELINE;

    int baseCalculation = BASELINE_RANK - distanceFromBaseline / DISTANCE_FROM_BASELINE_DIVISOR;

    if (isStrafing) {
      baseCalculation *= 2;
    }

    return baseCalculation;

  }

  @Override
  protected boolean validate() {

    return getActor().attemptMoveTo(getTarget());

  }

  @Override
  protected void apply() {

    if (getActorAt().area != getTarget().area) {
      addFlag(ActionFlag.ACTOR_CHANGED_AREA);
    }

  }

  @Override
  public Moving attemptRepeat() {
    if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
      return null;
    } else {
      return new Moving(getActor(), direction, isStrafing);
    }
  }

  @Override
  public Color getIndicatorColor() {
    return Color.PINK;
  }

}
