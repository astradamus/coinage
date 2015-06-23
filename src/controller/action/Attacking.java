package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import game.physical.PhysicalFlag;
import world.Coordinate;

import java.awt.*;

/**
 * Actors perform attacks to inflict damage upon other actors.
 */
public class Attacking extends Action {



  private final Actor victim;

  public Attacking(Actor actor, Coordinate target, Actor victim) {
    super(actor, target);
    this.victim = victim;
  }

  @Override
  public Color getIndicatorColor() {
    return Color.RED;
  }



  @Override
  public int calcDelayToPerform() {
    return 3;
  }

  @Override
  public int calcDelayToRecover() {
    return 2;
  }



  /**
   * Attacking will fail if the victim is already dead or if the victim is no longer at the
   * target location.
   */
  @Override
  protected boolean validate() {
    boolean playerIsAttacking = getPerformer() == Game.getActivePlayer().getActor();

    if (victim.hasFlag(PhysicalFlag.DEAD)) {
      if (playerIsAttacking) {
        EventLog.registerEvent(Event.INVALID_ACTION, "It's already dead.");
      }
      return false;
    }

    // todo clean this up \/ \/ \/
    if (!getTarget().getSquare().getAll().contains(victim)) {
      if (playerIsAttacking) {
        EventLog.registerEvent(Event.INVALID_ACTION, victim.getName()+" eluded your attack.");
      } else {
        EventLog.registerEventIfPlayerIsNear(getPerformer().getCoordinate(),Event.INVALID_ACTION,
            victim.getName()+" eluded "+ getPerformer().getName()+"'s attack.");
      }
      return false;
    }

    return true;
  }


  /**
   * Wound the victim, with severity based on the actor's muscle attribute. Damage ranges from
   * {@code muscle*3} to {@code muscle*10}
   */
  @Override
  protected void apply() {

    final int actorMuscleRank = getPerformer().readAttributeLevel(Attribute.MUSCLE).ordinal();

    final int damageBase  = actorMuscleRank * 3;
    final int damageRange = actorMuscleRank * 7;

    final double damage = damageBase + Game.RANDOM.nextInt(damageRange);

    victim.getHealth().wound(damage);

  }


}
