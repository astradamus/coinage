package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import game.Direction;
import game.physical.PhysicalFlag;
import world.World;

import java.awt.Color;

/**
 * Actors perform moves to change their own location in the world. Their movement speed is defined
 * by how long the delay to perform a movement is for them, which is derived from their reflex
 * attribute. This delay is magnified if the actor is not facing the direction in which it is trying
 * to move, albeit less so if the direction is immediately adjacent (i.e. northwest and northeast
 * for north).
 * <p>
 * Passing {@code true} to {@code isWalking} will reduce movement to one-third speed.
 */
public class Action_Move extends Action {

    private final static int BASELINE_RANK = 5;
    private final static int BEATS_AT_BASELINE = 4;
    private final static int DISTANCE_ADJUSTMENT_DIVISOR = 3;

    private static final int FOUR_LEGGED_FULL_SPEED_BONUS = 1;

    private final Direction movingIn;
    private final boolean isWalking;


    public Action_Move(Actor actor, Direction movingIn, boolean isWalking) {
        super(actor, actor.getCoordinate().offset(movingIn.relativeX, movingIn.relativeY));
        this.movingIn = movingIn;
        this.isWalking = isWalking;
    }


    @Override
    public Color getIndicatorColor() {
        return Color.WHITE;
    }


    /**
     * At {@code BASELINE_RANK} reflex, an actor will suffer a {@code BEATS_AT_BASELINE} action delay.
     * For every {@code DISTANCE_ADJUSTMENT_DIVISOR} ranks above or below {@code BASELINE_RANK}, the
     * actor suffers one less or one more beat of action delay, respectively.
     */
    @Override
    public int calcDelayToPerform() {

        // Take the actor's reflex rank.
        final int actorReflex = getActor().getAttributeRank(Attribute.REFLEX).ordinal();

        // Determine distance from BASELINE_RANK.
        final int distanceFromBaseline = actorReflex - BASELINE_RANK;

        // Subtract adjustment from baseline to get normal delay to perform.
        int calculatedDelay = BEATS_AT_BASELINE - (distanceFromBaseline / DISTANCE_ADJUSTMENT_DIVISOR);

        // Determine facing adjustment.
        if (isWalking) {
            return calculatedDelay * 3; // Walking always takes three times as long.
        }

        if (getIsMovingInFacedDirection()) {
            if (getActor().hasFlag(PhysicalFlag.FOUR_LEGGED)) {
                calculatedDelay -= FOUR_LEGGED_FULL_SPEED_BONUS;
            }
            return calculatedDelay; // Full speed, no adjustment necessary.
        }

        if (getIsMovingInAdjacentDirection()) {
            return (int) (calculatedDelay * 1.5); // Adjacent directions take 50% longer to move in.
        }

        else {
            return calculatedDelay * 2; // All other directions take twice as long to move in.
        }
    }


    /**
     * Action_Move will fail if the target coordinate is blocked or if the actor is somehow relocated
     * before completing the movement.
     */
    @Override
    protected boolean validate(World world) {

        if (!world.validateCoordinate(getTarget())) {
            return false;
        }

        final boolean targetIsBlocked = world.getSquare(getTarget()).isBlocked();
        final boolean performerHasMoved = !world.getSquare(getOrigin()).getAll().contains(getActor());

        return !targetIsBlocked && !performerHasMoved;
    }


    /**
     * Remove the actor from its original location, add it to the new location, and update its stored
     * coordinate accordingly. If this move crosses area borders, it gains the {@code
     * ACTOR_CHANGED_AREA} flag.
     */
    @Override
    protected void apply(World world) {

        final Actor performerActor = getActor();

        world.getSquare(getOrigin()).pull(performerActor);
        world.getSquare(getTarget()).put(performerActor);
        performerActor.setCoordinate(getTarget());

        if (world.getArea(getOrigin()) != world.getArea(getTarget())) {
            addFlag(ActionFlag.ACTOR_CHANGED_AREA);
        }
    }


    /**
     * Movement will always repeat on success unless {@code doNotRepeat()} is called.
     */
    @Override
    public Action attemptRepeat() {
        if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
            return null;
        }
        else {
            return new Action_Move(getActor(), movingIn, isWalking);
        }
    }


    private boolean getIsMovingInFacedDirection() {
        return getActor().getFacing() == movingIn;
    }


    private boolean getIsMovingInAdjacentDirection() {
        final Direction actorFacing = getActor().getFacing();
        return movingIn == actorFacing.getLeftNeighbor() || movingIn == actorFacing.getRightNeighbor();
    }
}