package controller.action;

import actor.Actor;
import game.io.better_ui.Event;
import game.io.better_ui.EventLog;
import game.physical.Physical;
import world.Coordinate;
import world.World;

/**
 * Actors perform placings to move items from their inventory to the world.
 */
public class Placing extends Action {

  private final Physical placingWhat;


  public Placing(Actor actor, Coordinate placingWhere, Physical placingWhat) {
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
   * Placing will fail if the target location is blocked, or if the item is no longer in the actor's
   * inventory at the time of execution.
   */
  @Override
  protected boolean validate(World world) {

    if (world.getSquare(getTarget()).isBlocked()) {

      if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
        EventLog.registerEvent(Event.FAILURE, "There's no room there.");
      }

      return false;
    }

    final boolean itemIsHeldByActor =
        getActor().getInventory().getItemsHeld().contains(placingWhat);

    if (!itemIsHeldByActor && hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
      EventLog
          .registerEvent(Event.FAILURE, "You can't seem to find the item you were trying to drop.");
    }

    return itemIsHeldByActor;
  }


  /**
   * Place the item at the target location.
   */
  @Override
  protected void apply(World world) {
    if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
      EventLog.registerEvent(Event.SUCCESS, "You have dropped " + placingWhat.getName() + ".");
    }
    getActor().getInventory().removeItem(placingWhat);
    world.getSquare(getTarget()).put(placingWhat);
  }
}