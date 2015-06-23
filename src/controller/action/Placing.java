package controller.action;

import actor.Actor;
import game.display.Event;
import game.display.EventLog;
import game.physical.Physical;
import world.Coordinate;

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
   * Placing will fail if the target location is blocked, or if the item is no longer in the
   * actor's inventory at the time of execution.
   */
  @Override
  protected boolean validate() {

    if (getTarget().getSquare().isBlocked()) {
      EventLog.registerEvent(Event.INVALID_ACTION, "There's no room there.");
      return false;
    }

    boolean itemIsHeldByActor = getPerformer().getInventory().getItemsHeld().contains(placingWhat);

    if (!itemIsHeldByActor){
      EventLog.registerEvent(Event.INVALID_ACTION,
          "You can't seem to find the item you were trying to drop.");
    }

    return itemIsHeldByActor;

  }


  /**
   * Place the item at the target location.
   */
  @Override
  protected void apply() {
    getPerformer().getInventory().removeItem(placingWhat);
    getTarget().getSquare().put(placingWhat);
  }


}
