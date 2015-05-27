package world;

import game.Physical;
import utils.Dimension;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A 2D List table of Physical lists sorted such that the 'visible' Physical in each list (the top)
 * is the Physical with the highest return from Physical.getVisualPriority().
 */
public final class Component_Physicals {

  private final Dimension size;
  private final Terrain[][] terrain;
  private final List<List<List<Physical>>> physicals;


  Component_Physicals(Dimension size, Terrain[][] terrain, Physical[][] physicals) {

    this.size = size;
    this.terrain = terrain;
    this.physicals = new ArrayList<>();

    for(int y = 0; y < physicals.length; y++) {
      this.physicals.add(new ArrayList<List<Physical>>());
      for(int x = 0; x < physicals[0].length; x++) {
        this.physicals.get(y).add(new ArrayList<Physical>());

        Physical physicalHere = physicals[y][x];
        if (physicalHere != null) {
          put(physicalHere, x, y);
        }
      }
    }

  }



  /**
   * Returns the Physical at the top of the list at the given coordinate, or the terrain at that
   * coordinate if no Physicals are found.
   */
  public Physical getPriorityPhysical(int localX, int localY) {
    if (!physicals.get(localY).get(localX).isEmpty()) {
      return physicals.get(localY).get(localX).get(0);
    } else {
      return terrain[localY][localX];
    }
  }

  /**
   * Returns true if the given coordinate contains a Blocking Physical.
   */
  public boolean getCoordinateIsBlocked(int localX, int localY) {
    for(Physical physical : physicals.get(localY).get(localX)) {
      if (physical.isBlocking()) {
        return true;
      }
    }
    return false;
  }



  /**
   * Sorts a Physical (by descending getVisualPriority()) into the Physicals list for the given
   * coordinate.
   */
  void put(Physical placing, int localX, int localY) {
      if (placing == null) {
        System.out.println("Attempted to put null to Component_Physicals.");
        return;
      }

      List<Physical> targetList = this.physicals.get(localY).get(localX);

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
   * the Physical was found and removed.
   */
  boolean pull(Physical pulling, int localX, int localY) {
    return this.physicals.get(localY).get(localX).remove(pulling);
  }


  public List<Physical> getAllPhysicalsAt(Point localCoordinate) {

    if (!size.getCoordinateIsWithinBounds(localCoordinate)) {
      return null;
    }

    List<Physical> physicals =
        new ArrayList<>(this.physicals.get(localCoordinate.y).get(localCoordinate.x));
    physicals.add(terrain[localCoordinate.y][localCoordinate.x]);
    return physicals;

  }

}
