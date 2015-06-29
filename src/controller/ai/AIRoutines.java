package controller.ai;

import actor.Actor;
import actor.attribute.Attribute;
import actor.attribute.Perception;
import actor.attribute.Rank;
import actor.stats.Health;
import controller.ActorController;
import controller.action.Action;
import controller.action.Moving;
import controller.action.TurnThenMove;
import game.Direction;
import game.Game;
import game.physical.PhysicalFlag;
import world.Area;
import world.Coordinate;

import java.util.Set;

/**
 * Contains common subroutines used in multiple packages.
 */
public class AIRoutines {

  /**
   * If we are already facing the given direction, just start moving. Otherwise, turn and then
   * move.
   */
  public static void turnThenMove(AIController puppet, Direction goal,
                                  boolean isWalking, boolean doNotRepeat) {

    final boolean alreadyFacingGoal = puppet.getActor().getFacing() == goal;

    final Action action;

    if (alreadyFacingGoal) {
      action = new Moving(puppet, goal, isWalking);
    }
    else {
      action = new TurnThenMove(puppet, goal, isWalking);
    }

    if (doNotRepeat) {
      action.doNotRepeat();
    }

    puppet.attemptAction(action);

  }


  /**
   * Makes the puppet sidestep the square directly in front of it. It will turn one direction
   * grade either to the left or the right (chosen at random), and step forward one square. This
   * is used as a quick way to get around obstacles in the absence of more advanced pathing.
   */
  public static void stepAroundBlockedSquare(AIController puppet) {

    Direction turningTowards = puppet.getActor().getFacing();

    if (Game.RANDOM.nextBoolean()) {
      turningTowards = turningTowards.getLeftNeighbor();
    }
    else {
      turningTowards = turningTowards.getRightNeighbor();
    }

    puppet.attemptAction(new TurnThenMove(puppet, turningTowards, false).doNotRepeat());

  }


  /**
   * Makes the puppet advance one square towards the given coordinate.
   */
  public static void approachOneStep(AIController puppet, Coordinate destination) {

    final Coordinate actorAt = puppet.getActor().getCoordinate();
    final Direction toPursue = actorAt.getDirectionTo(destination);

    turnThenMove(puppet, toPursue, false, true);

  }


  /**
   * Sweeps the area in which the puppet resides for other actor controllers. The first one it
   * finds within sensory range (as defined by the puppet's perception attribute)
   */
  public static void performSensoryScan(AIController puppet) {

    final Area area = puppet.getLocality();

    final Actor actor = puppet.getActor();
    final Coordinate actorAt = actor.getCoordinate();
    final Rank perceptionRank = actor.readAttributeLevel(Attribute.PERCEPTION);

    // Get all actor controllers in our area.
    final Set<ActorController> actorControllers =
        Game.getActiveControllers().getActorControllersInArea(area);


    for (ActorController scanTarget : actorControllers) {

      final Actor targetActor = scanTarget.getActor();

      // Scan through each, skipping any that fail one of the following tests.
      if  (

          // Don't react to self.
          scanTarget != puppet

          // Timid actors ignore each other.
          && !(actor.hasFlag(PhysicalFlag.TIMID)
          && targetActor.hasFlag(PhysicalFlag.TIMID))

          // Ignore dead actors. Actor might be dead because ActorControllers do not unregister
          // (and escape this loop) until they have been updated for their own turn.
          && !targetActor.hasFlag(PhysicalFlag.DEAD)) {


      // If we've passed the tests, determine if this actor controller is within either visual or
      // auditory range (in that order). If it is, react accordingly, if not, go to the next
      // actor controller in this area.

        final Coordinate targetAt = targetActor.getCoordinate();

        // Can we see the target?
        if (Perception.getCanSeeLocation(perceptionRank, actor.getFacing(), actorAt, targetAt)) {
          AIRoutines.evaluateOther(puppet, scanTarget);
        }

        // Can we hear the target?
        else if (Perception.getCanHearLocation(perceptionRank, actorAt, targetAt)) {
          puppet.exhibitBehavior(new AI_Investigate(puppet, targetAt, scanTarget));
        }

      }

    }

  }


  /**
   * Determines how the puppet should react to the given other. If either the puppet or the other
   * have the aggressive flag, the puppet will enter fight or flee. In other words, aggressives
   * will attack/flee everything they encounter, while non-aggressives will only attack/flee
   * aggressives.
   */
  public static void evaluateOther(AIController puppet, ActorController other) {

    if (other.getActor().hasFlag(PhysicalFlag.AGGRESSIVE)
        || puppet.getActor().hasFlag(PhysicalFlag.AGGRESSIVE)) {
      evaluateNewAggressor(puppet, other);
    }

  }


  /**
   * Determines how the puppet should react to an aggressor upon first encounter. Actors with the
   * timid flag will always retreat on this call. Other actors will call {@code
   * getShouldFleeCombat()} to determine if they should retreat. If they do not retreat, they
   * will begin fighting the aggressor.
   */
  public static void evaluateNewAggressor(AIController puppet, ActorController aggressor) {

    final AIBehavior response;

    if (puppet.getActor().hasFlag(PhysicalFlag.TIMID) || getShouldFleeCombat(puppet)) {
      response = new AI_Retreat(puppet, aggressor);
    }
    else {
      response = new AI_Fight(puppet, aggressor);
    }

    puppet.exhibitBehavior(response);

  }


  /**
   * Determines if the given puppet is up for a fight or if it should retreat to nurse its wounds.
   * Actors with the aggressive flag will let their health get lower before fleeing, while actors
   * with the timid flag will do the opposite. Actors with neither flag will retreat somewhere in
   * the middle.
   */
  public static boolean getShouldFleeCombat(AIController puppet) {

    final Actor actor = puppet.getActor();
    final Health health = actor.getHealth();

    if (actor.hasFlag(PhysicalFlag.AGGRESSIVE) && health.getFraction() < 0.20) {
      return true;
    }
    else if (actor.hasFlag(PhysicalFlag.TIMID) && health.getFraction() < 0.60) {
      return true;
    }
    else if (health.getFraction() < 0.40) {
      return true;
    }

    return false;

  }

}