package world;

import game.Physical;

import java.util.ArrayList;
import java.util.List;

/**
 * A 2D List table of Physical lists sorted such that the 'visible' Physical in each list (the top)
 * is the Physical with the highest "Visual Priority."
 */
public class WorldLayer_Physicals {

  private final List<List<List<Physical>>> physicals;

  public WorldLayer_Physicals(Physical[][] physicals) {

    this.physicals = new ArrayList<>();

    for(int y = 0; y < physicals.length; y++) {
      this.physicals.add(new ArrayList<List<Physical>>());
      for(int x = 0; x < physicals[0].length; x++) {
        this.physicals.get(y).add(new ArrayList<Physical>());

        Physical physicalHere = physicals[y][x];
        if (physicalHere != null) {
          putPhysical(x,y, physicalHere);
        }
      }
    }

  }

  /**
   * Sorts a Physical (by descending getVisualPriority()) into the Physicals list for the given
   * coordinate.
   */
  void putPhysical(int x, int y, Physical placing) {
      if (placing == null) {
        System.out.println("Attempted to put null to WorldLayer_Physicals.");
        return;
      }

      List<Physical> targetList = this.physicals.get(y).get(x);

      for (int i = 0; i < targetList.size(); i++) {
        Physical comparePhysical = targetList.get(i);

        if (placing.getVisualPriority() >= comparePhysical.getVisualPriority()) {
          targetList.add(i,placing);
          return;
        }
      }
      targetList.add(placing);
  }

  /**
   * Removes the given Physical from the Physicals list for the given coordinate. Returns true if
   * the Physical is found there.
   */
  boolean removePhysicalFrom(int x, int y, Physical removing) {
    return this.physicals.get(y).get(x).remove(removing);
  }

  /**
   * Returns the Physical with the highest visual priority at the given location, or null if none
   * are found.
   */
  Physical getPriorityPhysicalAt(int x, int y) {
    if (physicals.get(y).get(x).isEmpty()) {
      return null;
      } else {
      return physicals.get(y).get(x).get(0);
    }
  }

  public boolean getLocationIsBlocked(int toX, int toY) {
    for(Physical physical : physicals.get(toY).get(toX)) {
      if (physical.isBlocking()) {
        return true;
      }
    }
    return false;
  }
}
