package controller.action;

import actor.Actor;
import game.Direction;

/**
 *
 */
public class Action_TurnThenMove extends Action_Turn {

    private final boolean isWalking;


    public Action_TurnThenMove(Actor actors, Direction turningTowards, boolean isWalking) {
        super(actors, turningTowards);
        this.isWalking = isWalking;
    }


    @Override
    public Action attemptRepeat() {

        final boolean targetDirectionReached = getActor().getFacing() == turningTowards;

        Action next;

        if (!targetDirectionReached) {
            next = new Action_TurnThenMove(getActor(), turningTowards, isWalking);
        }
        else {
            next = new Action_Move(getActor(), turningTowards, isWalking);
        }

        if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
            next.doNotRepeat(); // Pass repeat cancellation along the chain, if there is one.
        }

        return next;
    }
}