package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import world.Coordinate;

import java.awt.*;

/**
 *
 */
public class Attack extends Action {

  private final Actor victim;

  public Attack(Actor actor, Coordinate target, Actor victim) {
    super(actor, target);
    this.victim = victim;
  }

  @Override
  public int calcDelayToPerform() {
    return 3;
  }

  @Override
  protected int calcDelayToRecover() {
    return 2;
  }

  @Override
  protected boolean validate() {
    boolean playerIsAttacking = getActor() == Game.getActivePlayer().getActor();

    if (victim.isDead()) {
      if (playerIsAttacking) {
        EventLog.registerEvent(Event.INVALID_ACTION, "It's already dead.");
      }
      return false;
    }

    if (!getTarget().getSquare().getAll().contains(victim)) {
      if (playerIsAttacking) {
        EventLog.registerEvent(Event.INVALID_ACTION, victim.getName()+" eluded your attack.");
      } else {
        EventLog.registerEventIfPlayerIsNear(getActor().getCoordinate(),Event.INVALID_ACTION,
            victim.getName()+" eluded "+getActor().getName()+"'s attack.");
      }
      return false;
    }

    return true;
  }

  @Override
  protected void apply() {
    int damageRange = getActor().readAttributeLevel(Attribute.MUSCLE).ordinal()*5;
    victim.getHealth().wound(Game.RANDOM.nextInt(damageRange));
  }

  @Override
  public Color getIndicatorColor() {
    return Color.RED;
  }

}
