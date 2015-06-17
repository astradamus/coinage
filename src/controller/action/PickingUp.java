package controller.action;

import actor.Actor;
import game.Physical;
import game.display.Event;
import game.display.EventLog;
import world.Coordinate;

import java.security.InvalidParameterException;

/**
 *
 */
public class PickingUp extends Action {

  private final Physical item;

  public PickingUp(Actor actor, Coordinate target, Physical item) {
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

    if (item.isImmovable()) {
      EventLog.registerEvent(Event.INVALID_ACTION, "That can't be picked up.");
      return false;
    }


    if (getTarget().getSquare().pull(item)) {
      return true;
    } else {
      EventLog.registerEvent(Event.INVALID_ACTION, "The thing you were reaching for is no longer there.");
      return false;
    }

  }

  @Override
  protected void apply() {

    getActor().getInventory().addItem(item);

  }

}
