package controller.action;

import controller.ActorController;
import game.display.EventLog;
import game.physical.Physical;
import thing.Thing;

import java.awt.Color;

/**
 *
 */
public class EquipWeapon extends Action {

  private final Physical weapon;

  private Thing validatedWeapon;

  public EquipWeapon(ActorController performer, Physical weapon) {
    super(performer, null);
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
  protected boolean validate() {

    if (weapon.getClass() == Thing.class) {
      validatedWeapon = (Thing) weapon;
      if (validatedWeapon.getWeaponComponent() != null) {
        return true;
      }
    }

    if (getPlayerIsPerformer()) {
      EventLog.registerEvent(Color.CYAN, "You can't equip " + weapon.getName() + ".");
    }

    return false;

  }


  /**
   * Turn the actor one direction grade towards the target direction.
   */
  @Override
  protected void apply() {
    getPerformer().getActor().setEquippedWeapon(validatedWeapon);

    if (getPlayerIsPerformer()) {
      EventLog.registerEvent(Color.CYAN, "You have equipped " + validatedWeapon.getName() + ".");
    }
  }


}