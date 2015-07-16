package actor.inventory;

import game.physical.Physical;
import thing.Thing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Inventory {

  private final List<Physical> itemsHeld;
  private final Thing naturalWeapon;
  private Thing equippedWeapon;


  public Inventory(Thing naturalWeapon) {
    itemsHeld = new ArrayList<>();
    this.naturalWeapon = naturalWeapon;
  }


  public boolean addItem(Physical item) {

    if (!itemsHeld.contains(item)) {

      itemsHeld.add(item);
      return true;
    }
    else {

      return false;
    }
  }


  public boolean removeItem(Physical item) {

    return itemsHeld.remove(item);
  }


  /**
   * Returns a new List containing all Physicals carried in this inventory. Modifications to this
   * List will not affect the actual contents of the Inventory.
   */
  public List<Physical> getItemsHeld() {
    return new ArrayList<>(itemsHeld);
  }


  /**
   * Returns the actor's equipped weapon, if it has one. Otherwise, its natural weapon.
   */
  public Thing getWeapon() {
    if (equippedWeapon != null) {
      return equippedWeapon;
    }
    else {
      return naturalWeapon;
    }
  }


  /**
   * Sets the actor's equipped weapon to the given thing. Does nothing if the given thing has no
   * weapon component.
   */
  public void setEquippedWeapon(Thing weapon) {
    if (weapon.getWeaponComponent() != null) {
      equippedWeapon = weapon;
    }
  }
}
