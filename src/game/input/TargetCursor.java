package game.input;

import game.Direction;
import game.Game;
import utils.Utils;
import world.Area;
import world.AreaCoordinate;
import world.Coordinate;
import world.World;

/**
 *
 */
public class TargetCursor {

  private final Coordinate areaOrigin;
  private final Integer areaRange;
  private Integer listSelectLength;

  private Direction cursorMovingIn = null;
  private int moveDelay = 0;

  private Coordinate target = null;
  private Integer listSelectIndex = null;

  private TargetCursor(Coordinate areaOrigin, Integer areaRange, Integer listSelectLength) {
    this.areaOrigin = areaOrigin;
    this.target = areaOrigin;
    this.areaRange = areaRange;
    this.listSelectLength = listSelectLength;
    if (listSelectLength != null) {
      this.listSelectIndex = 0;
    }
  }

  public static TargetCursor makeSquareTargeter(Coordinate targetOrigin, Integer areaRange) {

    return new TargetCursor(targetOrigin, areaRange, null);

  }

  public static TargetCursor makeListTargeter(Integer listSelectLength) {

    return new TargetCursor(null, null, listSelectLength);

  }

  public static TargetCursor makeSquareAndListTargeter(Coordinate targetOrigin, Integer areaRange) {
    return new TargetCursor(targetOrigin, areaRange, Game.getActiveWorld().getSquare(targetOrigin).getAll().size());
  }



  void onUpdate() {
    if (moveDelay > 0) {
      moveDelay--;
    }
    else if (target != null && cursorMovingIn != null) {

      final World world = Game.getActiveWorld();

      int relativeX = (target.globalX - areaOrigin.globalX) + cursorMovingIn.relativeX;
      int relativeY = (target.globalY - areaOrigin.globalY) + cursorMovingIn.relativeY;

      if (areaRange != null) {
        relativeX = Utils.clamp(relativeX,-areaRange,+areaRange);
        relativeY = Utils.clamp(relativeY,-areaRange,+areaRange);
      }

      Coordinate movingTo = areaOrigin.offset(relativeX, relativeY);

      if (movingTo != null) {
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