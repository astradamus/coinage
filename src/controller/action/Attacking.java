package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import controller.ActorController;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import game.physical.PhysicalFlag;
import world.Coordinate;

import java.awt.Color;

/**
 * Actors perform attacks to inflict damage upon other actors.
 */
public class Attacking extends Action {



  private final ActorController victim;

  public Attacking(ActorController performer, Coordinate victimWhere, ActorController victim) {
    super(performer, victimWhere);
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
    final boolean playerIsPerformer = getPerformer() == Game.getActivePlayer();

    final Actor performerActor = getPerformer().getActor();
    final Actor victimActor = victim.getActor();

    if (victimActor.hasFlag(PhysicalFlag.DEAD)) {
      if (playerIsPerformer) {
        EventLog.registerEvent(Event.INVALID_ACTION, "It's already dead.");

      }
      return false;
    }

    // todo clean this up \/ \/ \/
    final boolean targetEludedAttack = !getTarget().getSquare().getAll().contains(victimActor);
    if (targetEludedAttack) {


      if (playerIsPerformer) {
        EventLog.registerEvent(Event.INVALID_ACTION, victimActor.getName()+" eluded your attack.");

      } else {
        EventLog.registerEventIfPlayerIsNear(performerActor.getCoordinate(),Event.INVALID_ACTION,
            victimActor.getName()+" eluded "+ performerActor.getName()+"'s attack.");

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

    final int actorMuscleRank = getPerformer().getActor()
        .readAttributeLevel(Attribute.MUSCLE).ordinal();

    final int damageBase  = actorMuscleRank * 3 / 2;
    final int damageRange = actorMuscleRank * 7 / 2;

    final double damage = damageBase + Game.RANDOM.nextInt(damageRange);

    victim.getActor().getHealth().wound(damage);
    victim.onAttackSuffered(getPerformer());

  }


}