package actor.inventory;

import game.physical.Physical;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Inventory {

  private final List<Physical> itemsHeld;

  public Inventory() {

    itemsHeld = new ArrayList<>();

  }

  public boolean addItem(Physical item) {

    if (!itemsHeld.contains(item)) {

      itemsHeld.add(item);
      return true;

    } else {

      return false;

    }

  }

  public boolean removeItem(Physical item) {

    return itemsHeld.remove(item);

  }

  /**
   * @return A new List containing all Physicals carried in this inventory. Modifications to this
   * List will not affect the actual contents of the Inventory.
   */
  public List<Physical> getItemsHeld() {

    return new ArrayList<>(itemsHeld);

  }

}
