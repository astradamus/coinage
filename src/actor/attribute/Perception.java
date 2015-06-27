package actor.attribute;

import game.Direction;
import world.Coordinate;

/**
 *
 */
public class Perception {

  public static boolean getCanSeeLocation(Rank perceptionRank, Direction currentFacing,
                                          Coordinate from, Coordinate to) {


    final int visionDistance = perceptionRank.ordinal()*2;


    final int deltaX = Math.abs(from.globalX - to.globalX);
    final int deltaY = Math.abs(from.globalY - to.globalY);

    final boolean closeEnoughToSee = deltaX <= visionDistance && deltaY <= visionDistance;

    if (closeEnoughToSee) {

      final Direction fromTo = Direction.fromPointToPoint(
          from.globalX, from.globalY, to.globalX, to.globalY);

      // If we are either directly facing the target, or it is in our peripheral, return true.
      return currentFacing == fromTo
      || currentFacing.getLeftNeighbor() == fromTo
      || currentFacing.getRightNeighbor() == fromTo;

    }

    return false;

  }

  public static boolean getCanHearLocation(Rank perceptionRank, Coordinate from, Coordinate to) {

    final int hearingDistance = perceptionRank.ordinal();

    final int deltaX = Math.abs(from.globalX - to.globalX);
    final int deltaY = Math.abs(from.globalY - to.globalY);

    return (deltaX <= hearingDistance && deltaY <= hearingDistance);

  }

}
