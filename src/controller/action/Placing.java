package controller.action;

import actor.Actor;
import game.Physical;
import game.display.Event;
import game.display.EventLog;

/**
 *
 */
public class Placing extends Action {

  public Placing(Actor actor, ActionTarget<Physical> target) {
    super(actor, target);
  }

  @Override
  public int calcBeatsToPerform() {
    return 2;
  }

  @Override
  protected int calcBeatsToRecover() {
    return 1;
  }

  @Override
  protected boolean validate() {

    if (getActionTarget().getTargetAt().getSquare().isBlocked()) {
      EventLog.registerEvent(Event.INVALID_ACTION, "There's no room there.");
      return false;
    }


    if (getActor().getInventory().removeItem(getActionTarget().getTarget())){
      return true;
    } else {
      EventLog.registerEvent(Event.INVALID_ACTION,
          "You can't seem to find the thing you were trying to drop.");
      return false;
    }

  }

  @Override
  protected void apply() {

    getActionTarget().getTargetAt().getSquare().put(getActionTarget().getTarget());

  }

}
