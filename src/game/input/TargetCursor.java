package game.input;

import game.Direction;
import game.Game;
import utils.Utils;
import world.Coordinate;

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

    return new TargetCursor(targetOrigin, areaRange, targetOrigin.getSquare().getAll().size());

  }



  void onUpdate() {
    if (moveDelay > 0) {
      moveDelay--;
    }
    else if (target != null && cursorMovingIn != null) {

      Coordinate playerAt = Game.getActivePlayer().getCoordinate();

      int relativeX = (target.globalX - playerAt.globalX) + cursorMovingIn.relativeX;
      int relativeY = (target.globalY - playerAt.globalY) + cursorMovingIn.relativeY;

      if (areaRange != null) {
        relativeX = Utils.clamp(relativeX,-areaRange,+areaRange);
        relativeY = Utils.clamp(relativeY,-areaRange,+areaRange);
      }

      Coordinate movingTo = Game.getActiveWorld()
          .offsetCoordinateBySquares(playerAt,relativeX,relativeY);

      if (movingTo != null && areaOrigin.area == movingTo.area) {
        target = movingTo;
        moveDelay = 2;

        if (listSelectLength != null) {
          // if we're also targeting a list...
          listSelectLength = target.getSquare().getAll().size();
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
