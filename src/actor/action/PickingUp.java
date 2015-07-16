package actor.action;

import actor.Actor;
import game.io.display.Event;
import game.io.display.EventLog;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import world.Coordinate;
import world.World;

/**
 * Actors perform pickups to move items from the world to their inventory.
 */
public class PickingUp extends Action {

  private final Physical pickingUpWhat;


  public PickingUp(Actor actor, Coordinate pickingUpWhere, Physical pickingUpWhat) {
    super(actor, pickingUpWhere);
    this.pickingUpWhat = pickingUpWhat;
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
   * Picking up will fail if the item is immovable, or if the item is not found at the target
   * location.
   */
  @Override
  protected boolean validate(World world) {

    if (pickingUpWhat.hasFlag(PhysicalFlag.IMMOVABLE)) {

      if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
        EventLog.registerEvent(Event.FAILURE,
            "You couldn't seem to pick up " + pickingUpWhat.getName() + ".");
      }
      return false;
    }

    boolean itemIsAtTarget = world.getSquare(getTarget()).getAll().contains(pickingUpWhat);

    if (!itemIsAtTarget && hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
      EventLog.registerEvent(Event.FAILURE, "The thing you were reaching for is no longer there.");
    }

    return itemIsAtTarget;
  }


  /**
   * Upon success, the item is added to the actor's inventory.
   */
  @Override
  protected void apply(World world) {
    if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
      EventLog.registerEvent(Event.SUCCESS,
          "You have added " + pickingUpWhat.getName() + " to your inventory.");
    }
    world.getSquare(getTarget()).pull(pickingUpWhat);
    getActor().getInventory().addItem(pickingUpWhat);
  }
}