package world;

import game.Physical;

/**
 *
 */
public class World {

  final Area[][] areas;
  public final int worldAreasWide;
  public final int worldAreasTall;
  public final int areaWidth;
  public final int areaHeight;
  public final int globalWidth;
  public final int globalHeight;

  World(Area[][] areas) {

    this.areas = areas;
    worldAreasWide = areas[0].length;
    worldAreasTall = areas.length;
    areaWidth = areas[0][0].getWidth();
    areaHeight = areas[0][0].getHeight();
    globalWidth = worldAreasWide * areaWidth;
    globalHeight = worldAreasTall * areaHeight;

  }

  public void globalPlacePhysical(Physical spawning, int globalX, int globalY) {
    BreakResult bR = breakWorldLocation(globalX,globalY);
    bR.area.physicals.putPhysical(bR.localX, bR.localY, spawning);
  }

  public boolean globalMovePhysical(Physical moving, int fromGlobalX, int fromGlobalY,
                                    int toGlobalX, int toGlobalY) {

    if (toGlobalX < 0 || toGlobalX >= globalWidth || toGlobalY < 0 || toGlobalY >= globalHeight) {
      return false; // don't allow movement to points outside the world
    }
    
    BreakResult from = breakWorldLocation(fromGlobalX, fromGlobalY);
    BreakResult to = breakWorldLocation(toGlobalX, toGlobalY);

    if (to.area.physicals.getLocationIsBlocked(to.localX, to.localY)) {
      return false;
    }
    if (from.area.physicals.removePhysicalFrom(from.localX, from.localY, moving)) {
      to.area.physicals.putPhysical(to.localX,to.localY,moving);
      return true;
    } else {
      System.out.println("Attempted to move Physical that was not found at fromGlobalX/fromGlobalY.");
      return false;
    }

  }

  public boolean globalIsBlocked(int globalX, int globalY) {
    BreakResult bR = breakWorldLocation(globalX,globalY);
    return bR.area.physicals.getLocationIsBlocked(bR.localX, bR.localY);
  }

  public Area getAreaFromGlobalCoordinate(int globalX, int globalY) {
    return breakWorldLocation(globalX,globalY).area;
  }


  /**
   * Translates a global coordinate into an Area and local coordinates within that area.
   */
  BreakResult breakWorldLocation(int worldX, int worldY) {
    Area area = areas[worldY / areaHeight][worldX / areaWidth];
    int localX = worldX % areaWidth;
    int localY = worldY % areaHeight;
    return new BreakResult(area,localX,localY);
  }

  class BreakResult {
    public final Area area;
    public final int localX;
    public final int localY;

    public BreakResult(Area area, int localX, int localY) {
      this.area = area;
      this.localX = localX;
      this.localY = localY;
    }
  }

}
