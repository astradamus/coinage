package controller.action;

import actor.Actor;
import game.io.display.EventLog;
import game.physical.Physical;
import thing.Thing;
import world.World;

import java.awt.Color;

/**
 *
 */
public class EquipWeapon extends Action {

  private final Physical weapon;
  private Thing validatedWeapon;

  public EquipWeapon(Actor actor, Physical weapon) {
    super(actor, null);
    this.weapon = weapon;
  }


  @Override
  public int calcDelayToPerform() {
    return 2;
  }

  @Override
  public int calcDelayToRecover() {
    return 1;
  }

  /**
   * Equipping fails if the target physical is not a weapon.
   */
  @Override
  protected boolean validate(World world) {

    if (weapon.getClass() == Thing.class) {
      validatedWeapon = (Thing) weapon;
      if (validatedWeapon.getWeaponComponent() != null) {
        return true;
      }
    }

    if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
      EventLog.registerEvent(Color.CYAN, "You can't equip " + weapon.getName() + ".");
    }

    return false;

  }

  /**
   * Set the actor's equipped weapon. If this action was acted by the player, log an appropriate
   * message.
   */
  @Override
  protected void apply(World world) {

    getActor().setEquippedWeapon(validatedWeapon);

    if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
      EventLog.registerEvent(Color.CYAN, "You have equipped " + validatedWeapon.getName() + ".");
    }

  }

}