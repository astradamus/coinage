package controller.action;

import controller.ActorController;
import game.Direction;

/**
 *
 */
public class TurnThenMove extends Turning {


  private final boolean isWalking;

  public TurnThenMove(ActorController performer, Direction turningTowards, boolean isWalking) {
    super(performer, turningTowards);
    this.isWalking = isWalking;
  }


  @Override
  public Action attemptRepeat() {

    final boolean targetDirectionReached = getPerformer().getActor().getFacing() == turningTowards;

    Action next;

    if (!targetDirectionReached) {
      next = new TurnThenMove(getPerformer(), turningTowards, isWalking);
    } else {
      next = new Moving(getPerformer(), turningTowards, isWalking);
    }


    if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
      next.doNotRepeat(); // Pass repeat cancellation along the chain, if there is one.
    }

    return next;

  }

}