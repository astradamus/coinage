package controller.action;

import actor.Actor;
import game.Direction;
import world.World;

/**
 * Actors perform turns to change their facing direction. Actors can only turn one direction grade
 * at a time, but attempts to turn more than one grade will automatically repeat until the target is
 * reached.
 */
public class Action_Turn extends Action {

    final Direction turningTowards;


    public Action_Turn(Actor actor, Direction turningTowards) {
        super(actor, null);
        this.turningTowards = turningTowards;
    }


    /**
     * Action_Turn cannot currently fail.
     */
    @Override
    protected boolean validate(World world) {
        return true;
    }


    /**
     * Turn the actor one direction grade towards the target direction.
     */
    @Override
    protected void apply(World world) {

        final Direction actorFacing = getActor().getFacing();

        final int difference = actorFacing.ordinal() - turningTowards.ordinal();

        if (difference == 0) {
            return;
        }

        // Evaluate whether turning left or right will get there faster.
        if ((difference > 0 && difference <= 4) || difference < -4) {
            getActor().setFacing(actorFacing.getLeftNeighbor());
        }
        else {
            getActor().setFacing(actorFacing.getRightNeighbor());
        }
    }


    /**
     * Continue turning until the target direction is reached. Cancelling repeat does not interrupt
     * the turn, and if we were to attempt a move after the turn, we will still always perform one
     * movement in the target direction. The cancellation applies to any future moves.
     *
     * @return The next action, or null if none should follow.
     */
    @Override
    public Action attemptRepeat() {

        final boolean targetDirectionReached = getActor().getFacing() == turningTowards;

        if (targetDirectionReached) {
            return null;
        }
        else {

            final Action_Turn next = new Action_Turn(getActor(), turningTowards);

            if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
                next.doNotRepeat(); // Pass repeat cancellation along the chain, if there is one.
            }

            return next;
        }
    }
}