package actor.attribute;

import game.Direction;
import world.Coordinate;
import world.MapCoordinate;

/**
 *
 */
public class Perception {

  /**
   * Evaluates whether or not coordinate {@code to} can be seen from coordinate {@code from} by an
   * actor with the given {@code perceptionRank} facing direction {@code currentFacing}. For each
   * point of perception, an actor may see across two additional squares' distance.
   */
  public static boolean getCanSeeLocation(Rank perceptionRank, Direction currentFacing,
      Coordinate from, Coordinate to) {

    final int visionRange = perceptionRank.ordinal() * 2;
    final int globalDistance = from.getDistance(to);

    if (globalDistance <= visionRange) {

      final Direction fromTo = from.getDirectionTo(to);

      // If we are facing the target or it is in our peripheral, return true.
      return currentFacing == fromTo || currentFacing.getLeftNeighbor() == fromTo
          || currentFacing.getRightNeighbor() == fromTo;
    }

    else {
      return false;
    }
  }


  /**
   * Evaluates whether or not a noise at the given {@code to} coordinate can be heard from the given
   * {@code from} coordinate by an actor with the given {@code perceptionRank}. For each point of
   * perception, an actor may hear across one additional square's distance.
   */
  public static boolean getCanHearLocation(Rank perceptionRank, Coordinate from, Coordinate to) {

    final int hearingRange = perceptionRank.ordinal();
    final int globalDistance = from.getDistance(to);

    return (globalDistance <= hearingRange);
  }


  /**
   * Evaluates whether or not an actor at the given {@code to} coordinate can be tracked from the
   * given {@code from} coordinate by an actor with the given {@code perceptionRank}. For each four
   * points of perception, an actor may track across one additional area's distance. Actors with
   * less than four points of perception can still always track a minimum of one area's distance--in
   * other words, all actors can track actors in the area they are in and any immediately adjacent
   * to it.
   */
  public static boolean getCanTrackLocation(Rank perceptionRank, MapCoordinate from,
      MapCoordinate to) {

    final int trackingRange = Math.max(1, perceptionRank.ordinal() / 4);
    final int worldDistance = from.getDistance(to);

    return (worldDistance <= trackingRange);
  }
}