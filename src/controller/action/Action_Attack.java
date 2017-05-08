package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import actor.attribute.Rank;
import game.Game;
import game.io.display.Event;
import game.io.display.EventLog;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import thing.Thing;
import thing.WeaponComponent;
import world.Coordinate;
import world.World;

import java.awt.Color;

/**
 * Actors perform attacks to inflict damage upon other actors.
 */
public class Action_Attack extends Action {

    private Actor victim;


    public Action_Attack(Actor actor, Coordinate target) {
        super(actor, target);
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
     * Action_Attack will fail if there is no living actor at the target location.
     */
    @Override
    protected boolean validate(World world) {

        // Check if there's a victim in the target square for the attack to hit.
        victim = getLiveTargetAt(world, getTarget());

        // If we have a victim, then this attack is a hit.
        final boolean attackHit = victim != null;

        if (!attackHit && hasFlag(ActionFlag.ACTOR_IS_PLAYER)) {

            final String attackTypeString =
                    getActor().getActiveWeapon().getWeaponComponent().getDamageType().getAttackString();

            EventLog.registerEvent(Event.FAILURE, "Your " + attackTypeString + " has hit naught but air.");
        }

        return attackHit;
    }


    /**
     * Wound the victim with the weapon equipped by this actor.
     */
    @Override
    protected void apply(World world) {

        final Thing weapon = getActor().getActiveWeapon();
        final WeaponComponent weaponComponent = weapon.getWeaponComponent();

        // Determine how much damage this attack will do.
        final Rank muscle = getActor().getAttributeRank(Attribute.MUSCLE);
        final int damage = weaponComponent.calculateDamageRange(muscle).getRandomWithin(Game.RANDOM);

        // Construct the event log string for this attack.
        final String hitString = weaponComponent.getDamageType().getHitString();

        String victimName = victim.getName();
        if (hasFlag(ActionFlag.TARGET_IS_PLAYER)) {
            victimName = "YOU";
        }

        final String messageA = hitString + " " + victimName + " with ";
        final String messageB = weapon.getName() + " for " + Integer.toString(damage) + " damage.";

        String message;

        if (hasFlag(ActionFlag.ACTOR_IS_PLAYER)) {
            message = "You have " + messageA + "your " + messageB;
        }
        else {
            message = getActor().getName() + " has " + messageA + "its " + messageB;
        }

        // Log the message if the player is in this area.
        EventLog.registerEventIfPlayerIsLocal(victim.getCoordinate(), Event.ACTOR_WOUNDED, message);

        // Apply the damage to the victim and notify the victim's controller.
        victim.getHealth().wound(damage);
        victim.getActorObserver().onVictimized(getActor());
    }


    /**
     * @return The living actor occupying the target coordinate, or null if there isn't one.
     */
    private Actor getLiveTargetAt(World world, Coordinate coordinate) {

        final Physical targetPhysical = world.getSquare(coordinate).getAll().get(0);
        if (!targetPhysical.hasFlag(PhysicalFlag.DEAD) && targetPhysical.getClass() == Actor.class) {
            return (Actor) targetPhysical;
        }
        else {
            return null;
        }
    }
}