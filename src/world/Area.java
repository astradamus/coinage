package world;

import game.Physical;

/**
 * Contains the Physical information for the game. The way the world looks and the locations of
 * any placed Physicals. Many/most generic Things are stored only in the listings here.
 */
public final class Area {

  private final int width;
  private final int height;

  private final Terrain[][] terrain;
  final AreaLayer_Physicals physicals;


  Area(Terrain[][] terrain, Physical[][] physicals) {
    if (physicals.length != terrain.length || physicals[0].length != terrain[0].length) {
      throw new IllegalArgumentException("terrain AND physicals MUST BE THE SAME DIMENSIONS!");
    }

    this.width = terrain[0].length;
    this.height = terrain.length;
    this.terrain = terrain;

    this.physicals = new AreaLayer_Physicals(physicals);

  }



  public void placePhysical(Physical spawning, int x, int y) {
    physicals.putPhysical(x,y,spawning);
  }

  public void removePhysical(Physical removing, int fromX, int fromY) {
    if (!physicals.removePhysicalFrom(fromX, fromY, removing)) {
      System.out.println("Attempted to remove Physical that was not found at fromX/fromY.");
    }
  }

  public boolean movePhysical(Physical moving, int fromX, int fromY, int toX, int toY) {
    if (physicals.getLocationIsBlocked(toX, toY)) {
      return false;
    }
    if (physicals.removePhysicalFrom(fromX, fromY, moving)) {
      physicals.putPhysical(toX,toY,moving);
      return true;
    } else {
      System.out.println("Attempted to move Physical that was not found at fromX/fromY.");
      return false;
    }
  }

  public boolean isBlocked(int x, int y) {
    return physicals.getLocationIsBlocked(x, y);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Terrain getTile(int x, int y) {
    return terrain[y][x];
  }

  public Physical getPriorityPhysical(int x, int y) {
    Physical priority = physicals.getPriorityPhysicalAt(x, y);
    if (priority == null) {
      priority = getTile(x,y);
    }
    return priority;
  }
}