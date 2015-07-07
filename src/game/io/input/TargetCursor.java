package game.io.input;

import game.Direction;
import utils.Utils;
import world.Coordinate;
import world.World;

/**
 *
 */
public class TargetCursor {

  private final World world;

  private final Coordinate areaOrigin;
  private final Integer areaRange;
  private Integer listSelectLength;

  private Direction cursorMovingIn = null;
  private int moveDelay = 0;

  private Coordinate target = null;
  private Integer listSelectIndex = null;

  private TargetCursor(World world, Coordinate areaOrigin, Integer areaRange, Integer listSelectLength) {
    this.world = world;
    this.areaOrigin = areaOrigin;
    this.target = areaOrigin;
    this.areaRange = areaRange;
    this.listSelectLength = listSelectLength;
    if (listSelectLength != null) {
      this.listSelectIndex = 0;
    }
  }

  public static TargetCursor makeSquareTargeter(World world, Coordinate targetOrigin, Integer areaRange) {
    return new TargetCursor(world, targetOrigin, areaRange, null);
  }

  public static TargetCursor makeListTargeter(World world, Integer listSelectLength) {
    return new TargetCursor(world, null, null, listSelectLength);
  }

  public static TargetCursor makeSquareAndListTargeter(World world, Coordinate targetOrigin, Integer areaRange) {
    return new TargetCursor(world, targetOrigin, areaRange, world.getSquare(targetOrigin).getAll().size());
  }



  void onUpdate() {
    if (moveDelay > 0) {
      moveDelay--;
    }
    else if (target != null && cursorMovingIn != null) {

      int relativeX = (target.globalX - areaOrigin.globalX) + cursorMovingIn.relativeX;
      int relativeY = (target.globalY - areaOrigin.globalY) + cursorMovingIn.relativeY;

      if (areaRange != null) {
        relativeX = Utils.clamp(relativeX,-areaRange,+areaRange);
        relativeY = Utils.clamp(relativeY,-areaRange,+areaRange);
      }

      Coordinate movingTo = areaOrigin.offset(relativeX, relativeY);

      if (movingTo != null && world.validateCoordinate(movingTo)) {
        target = movingTo;
        moveDelay = 2;

        if (listSelectLength != null) {
          // if we're also targeting a list...
          listSelectLength = world.getSquare(target).getAll().size();
          listSelectIndex = 0;
        }
      }

    }
  }

  public void scrollSelection(int deltaY) {
    if (listSelectLength == null) {
      return; // ignore scrolls when we're not targeting a list
    }
    listSelectIndex = Utils.modulus(listSelectIndex + deltaY, listSelectLength);
  }

  public void setCursorMovingIn(Direction cursorMovingIn) {
    this.cursorMovingIn = cursorMovingIn;
  }


  public Coordinate getTarget() {
    return target;
  }

  public Integer getListSelectIndex() {
    return listSelectIndex;
  }

}