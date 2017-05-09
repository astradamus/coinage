package controller.action;

import actor.Actor;
import game.io.display.Event;
import game.io.display.EventLog;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import world.GlobalCoordinate;
import world.World;

/**
 * Actors perform collects to move items from the world to their inventory.
 */
public class Action_Collect extends Action {

    private final Physical thingToCollect;


    public Action_Collect(Actor actor, Physical thingToCollect, GlobalCoordinate whereThingIs) {
        super(actor, whereThingIs);
        this.thingToCollect = thingToCollect;
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
     * Action_Collect will fail if the item is immovable, or if the item is not found at the target location.
     */
    @Override
    protected boolean validate(World world) {

        if (thingToCollect.hasFlag(PhysicalFlag.IMMOVABLE)) {

            if (hasFlag(ActionFlag.ACTOR_IS_PLAYER)) {
                final String message = "You couldn't seem to collect " + thingToCollect.getName() + ".";
                EventLog.registerEvent(Event.FAILURE, message);
            }
            return false;
        }

        boolean itemIsAtTarget = world.getSquare(getTarget()).getAll().contains(thingToCollect);

        if (!itemIsAtTarget && hasFlag(ActionFlag.ACTOR_IS_PLAYER)) {
            EventLog.registerEvent(Event.FAILURE, "The thing you were reaching for is no longer there.");
        }

        return itemIsAtTarget;
    }


    /**
     * Upon success, the item is added to the actor's inventory.
     */
    @Override
    protected void apply(World world) {
        if (hasFlag(ActionFlag.ACTOR_IS_PLAYER)) {
            final String message = "You have added " + thingToCollect.getName() + " to your inventory.";
            EventLog.registerEvent(Event.SUCCESS, message);
        }
        world.getSquare(getTarget()).pull(thingToCollect);
        getActor().getInventory().addItem(thingToCollect);
    }
}