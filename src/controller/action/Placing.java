package controller.action;

import actor.Actor;
import game.Physical;
import game.display.Event;
import game.display.EventLog;
import world.Coordinate;

/**
 *
 */
public class Placing extends Action {

  private final Physical item;

  public Placing(Actor actor, Coordinate target, Physical item) {
    super(actor, target);
    this.item = item;
  }

  @Override
  public int calcDelayToPerform() {
    return 1;
  }

  @Override
  protected int calcDelayToRecover() {
    return 1;
  }

  @Override
  protected boolean validate() {

    if (getTarget().getSquare().isBlocked()) {
      EventLog.registerEvent(Event.INVALID_ACTION, "There's no room there.");
      return false;
    }


    if (getActor().getInventory().removeItem(item)){
      return true;
    } else {
      EventLog.registerEvent(Event.INVALID_ACTION,
          "You can't seem to find the thing you were trying to drop.");
      return false;
    }

  }

  @Override
  protected void apply() {

    getTarget().getSquare().put(item);

  }

}
