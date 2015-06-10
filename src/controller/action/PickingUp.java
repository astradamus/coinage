package controller.action;

import actor.Actor;
import game.display.Event;
import game.display.EventLog;
import world.Coordinate;

import java.security.InvalidParameterException;

/**
 *
 */
public class PickingUp extends Action {

  public PickingUp(Actor actor, ActionTarget target) {
    super(actor, target);

    if (getActor().getInventory() == null) {
      throw new InvalidParameterException("Given actor has no inventory.");
    }
  }

  @Override
  protected int calcBeatsToPerform() {
    return 3;
  }

  @Override
  protected boolean validate() {

    if (getActionTarget().getTarget().isImmovable()) {
      EventLog.registerEvent(Event.INVALID_ACTION, "That can't be picked up.");
      return false;
    }


    if (getActionTarget().getTargetAt().getSquare().pull(getActionTarget().getTarget())) {
      return true;
    } else {
      EventLog.registerEvent(Event.INVALID_ACTION, "The thing you were reaching for is no longer there.");
      return false;
    }

  }

  @Override
  protected void apply() {

    getActor().getInventory().addItem(getActionTarget().getTarget());

  }

}
