package controller.action;

import actor.Actor;
import game.io.display.Event;
import game.io.display.EventLog;
import game.physical.Physical;
import world.GlobalCoordinate;
import world.World;

/**
 * Actors perform placings to move items from their inventory to the world.
 */
public class Action_Place extends Action {

    private final Physical placingWhat;


    public Action_Place(Actor actor, GlobalCoordinate placingWhere, Physical placingWhat) {
        super(actor, placingWhere);
        this.placingWhat = placingWhat;
    }


    @Override
    public int calcDelayToPerform() {
        return 1;
    }


    @Override
    public int calcDelayToRecover() {
        return 1;
    }


    /**
     * Action_Place will fail if the target location is blocked, or if the item is no longer in the actor's
     * inventory at the time of execution.
     */
    @Override
    protected boolean validate(World world) {

        if (world.getSquare(getTarget()).isBlocked()) {

            if (hasFlag(ActionFlag.ACTOR_IS_PLAYER)) {
                EventLog.registerEvent(Event.FAILURE, "There's no room there.");
            }

            return false;
        }

        final boolean itemIsHeldByActor =
                getActor().getInventory().getItemsHeld().contains(placingWhat);

        if (!itemIsHeldByActor && hasFlag(ActionFlag.ACTOR_IS_PLAYER)) {
            EventLog.registerEvent(Event.FAILURE, "You can't seem to find the item you were trying to drop.");
        }

        return itemIsHeldByActor;
    }


    /**
     * Place the item at the target location.
     */
    @Override
    protected void apply(World world) {
        if (hasFlag(ActionFlag.ACTOR_IS_PLAYER)) {
            EventLog.registerEvent(Event.SUCCESS, "You have dropped " + placingWhat.getName() + ".");
        }
        getActor().getInventory().removeItem(placingWhat);
        world.getSquare(getTarget()).put(placingWhat);
    }
}