package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import game.Game;
import game.display.Event;
import game.display.EventLog;

/**
 *
 */
public class Attack extends Action {

  public Attack(Actor actor, ActionTarget<Actor> target) {
    super(actor, target);
  }

  @Override
  public int calcBeatsToPerform() {
    return 3;
  }

  @Override
  protected int calcBeatsToRecover() {
    return 2;
  }

  @Override
  protected boolean validate() {
    boolean playerIsAttacking = getActor() == Game.getActivePlayer().getActor();

    Actor target = (Actor) getActionTarget().getTarget();
    if (target.isDead()) {
      if (playerIsAttacking) {
        EventLog.registerEvent(Event.INVALID_ACTION, "It's already dead.");
      }
      return false;
    }

    if (!getActionTarget().getTargetAt().getSquare().getAll().contains(target)) {
      if (playerIsAttacking) {
        EventLog.registerEvent(Event.INVALID_ACTION, target.getName()+" eluded your attack.");
      } else {
        EventLog.registerEventIfPlayerIsNear(getActor().getCoordinate(),Event.INVALID_ACTION,
            target.getName()+" eluded "+getActor().getName()+"'s attack.");
      }
      return false;
    }

    return true;
  }

  @Override
  protected void apply() {
    int damageRange = getActor().readAttributeLevel(Attribute.MUSCLE).ordinal()*5;
    ((Actor)getActionTarget().getTarget()).getHealth().wound(Game.RANDOM.nextInt(damageRange));
  }

}
