package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import actor.attribute.Rank;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import thing.Thing;
import thing.WeaponComponent;
import world.Coordinate;

import java.awt.Color;

/**
 * Actors perform attacks to inflict damage upon other actors.
 */
public class Attacking extends Action {

  private final Actor intendedVictim;
  private Actor actualVictim;

  public Attacking(Actor actor, Coordinate attackTarget) {
    super(actor, attackTarget);
    this.intendedVictim = getLiveTargetAt(attackTarget);
  }

  @Override
  public Color getIndicatorColor() {
    return Color.RED;
  }


  @Override
  public int calcDelayToPerform() {
    final Thing equippedWeapon = getActor().getActiveWeapon();
    return equippedWeapon.getWeaponComponent()
        .calcAttackSpeed(getActor().getAttributeRank(Attribute.REFLEX));
  }

  @Override
  public int calcDelayToRecover() {
    final Thing equippedWeapon = getActor().getActiveWeapon();
    return equippedWeapon.getWeaponComponent()
        .calcRecoverySpeed(getActor().getAttributeRank(Attribute.REFLEX));
  }


  /**
   * Attacking will fail if the intendedVictim is already dead or if the intendedVictim is no
   * longer at the target location.
   */
  @Override
  protected boolean validate() {

    if (intendedVictim != null && !intendedVictim.hasFlag(PhysicalFlag.DEAD)) {

      final boolean intendedVictimHasMoved =
          !getTarget().getSquare().getAll().contains(intendedVictim);

      if (!intendedVictimHasMoved) {
        // Our intended victim is right where we want them.
        actualVictim = intendedVictim;
      }

    }


    // If actualVictim is still null, then we either had no intended victim, or they're gone.
    final boolean intendedVictimHasMovedOrWasNotProvided = actualVictim == null;

    if (intendedVictimHasMovedOrWasNotProvided) {
      // Check if there's a new victim in the target square for the attack to hit.
      actualVictim = getLiveTargetAt(getTarget());
    }


    // If actualVictim is STILL null, then this attack is a miss.
    final boolean attackWillHitSomeone = actualVictim != null;

    if (!attackWillHitSomeone && getPlayerIsActor()) {

      final String attackTypeString = getActor().getActiveWeapon()
          .getWeaponComponent().getDamageType().getAttackString();

        if (intendedVictim == null) {
          EventLog.registerEvent(Event.INVALID_ACTION, "Your "+attackTypeString+" has hit naught but air.");
        } else {
          EventLog.registerEvent(Event.INVALID_ACTION,
              intendedVictim.getName() + " eluded your "+attackTypeString+".");
        }

    }

    return attackWillHitSomeone;

  }

  /**
   * Wound the victim with the weapon equipped by this actor.
   */
  @Override
  protected void apply() {

    final Thing weapon = getActor().getActiveWeapon();
    final WeaponComponent weaponComponent = weapon.getWeaponComponent();

    // Determine how much damage this attack will do.
    final Rank muscle = getActor().getAttributeRank(Attribute.MUSCLE);
    final int damage = weaponComponent.calculateDamageRange(muscle).getRandomWithin(Game.RANDOM);


    // Construct the event log string for this attack.
    final String hitString = weaponComponent.getDamageType().getHitString();

    String victimName = actualVictim.getName();
    if (actualVictim == Game.getActivePlayerActor()) {
      victimName = "you";
    }

    final String messageA = hitString + " " + victimName + " with ";
    final String messageB = weapon.getName() + " for " + Integer.toString(damage) + " damage.";

    String message;

    if (getPlayerIsActor()) {
      message = "You have " + messageA + "your " + messageB;
    } else {
      message = getActor().getName() + " has "+ messageA + "its "+ messageB;
    }


    // Log the message if the player is in this area.
    EventLog.registerEventIfPlayerIsNear(actualVictim.getCoordinate(),
        Event.ACTOR_WOUNDED, message);

    // Apply the damage to the victim and notify the victim's controller.
    actualVictim.getHealth().wound(damage);
    actualVictim.getObserver().onVictimized(getActor());

  }

  /**
   * @return The living actor occupying the target coordinate, or null if there isn't one.
   */
  private  Actor getLiveTargetAt(Coordinate coordinate) {

    final Physical targetPhysical = coordinate.getSquare().getAll().get(0);
    if (!targetPhysical.hasFlag(PhysicalFlag.DEAD) && targetPhysical.getClass() == Actor.class) {
      return (Actor) targetPhysical;
    }
    else {
      return null;
    }

  }

}