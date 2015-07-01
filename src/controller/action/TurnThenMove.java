package controller.action;

import actor.Actor;
import game.Direction;

/**
 *
 */
public class TurnThenMove extends Turning {

  private final boolean isWalking;

  public TurnThenMove(Actor actors, Direction turningTowards, boolean isWalking) {
    super(actors, turningTowards);
    this.isWalking = isWalking;
  }


  @Override
  public Action attemptRepeat() {

    final boolean targetDirectionReached = getActor().getFacing() == turningTowards;

    Action next;

    if (!targetDirectionReached) {
      next = new TurnThenMove(getActor(), turningTowards, isWalking);
    } else {
      next = new Moving(getActor(), turningTowards, isWalking);
    }


    if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
      next.doNotRepeat(); // Pass repeat cancellation along the chain, if there is one.
    }

    return next;

  }

}