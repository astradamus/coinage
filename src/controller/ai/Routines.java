package controller.ai;

import actor.Actor;
import actor.attribute.Attribute;
import actor.attribute.Perception;
import actor.attribute.Rank;
import actor.stats.Health;
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
public class Routines {

  /**
   * If we are already facing the given direction, just start moving. Otherwise, turn and then
   * move.
   */
  public static void turnThenMove(AiActorAgent agent, Direction goal,
                                  boolean isWalking, boolean doNotRepeat) {

    final Actor actor = agent.getActor();
    final boolean alreadyFacingGoal = actor.getFacing() == goal;

    final Action action;

    if (alreadyFacingGoal) {
      action = new Moving(actor, goal, isWalking);
    }
    else {
      action = new TurnThenMove(actor, goal, isWalking);
    }

    if (doNotRepeat) {
      action.doNotRepeat();
    }

    actor.attemptAction(action);

  }


  /**
   * Makes the agent sidestep the square directly in front of it. It will turn one direction
   * grade either to the left or the right (chosen at random), and step forward one square. This
   * is used as a quick way to get around obstacles in the absence of more advanced pathing.
   */
  public static void stepAroundBlockedSquare(AiActorAgent agent) {

    final Actor actor = agent.getActor();

    Direction turningTowards = actor.getFacing();

    if (Game.RANDOM.nextBoolean()) {
      turningTowards = turningTowards.getLeftNeighbor();
    }
    else {
      turningTowards = turningTowards.getRightNeighbor();
    }

    actor.attemptAction(new TurnThenMove(actor, turningTowards, false).doNotRepeat());

  }


  /**
   * Makes the agent advance one square towards the given coordinate.
   */
  public static void approachOneStep(AiActorAgent agent, Coordinate destination) {

    final Coordinate actorAt = agent.getActor().getCoordinate();
    final Direction toPursue = actorAt.getDirectionTo(destination);

    turnThenMove(agent, toPursue, false, true);

  }


  /**
   * Sweeps the area in which the agent resides for other actor controllers. The first one it
   * finds within sensory range (as defined by the agent's perception attribute)
   */
  public static void performSensoryScan(AiActorAgent agent) {

    final Area area = agent.getWorld().getArea(agent.getLocality());

    final Actor actor = agent.getActor();
    final Coordinate actorAt = actor.getCoordinate();
    final Rank perceptionRank = actor.getAttributeRank(Attribute.PERCEPTION);

    // Get all actor controllers in our area.
    final Set<Actor> localActors = Game.getActiveControllers().getActorsInArea(area);


    for (Actor scanTarget : localActors) {

      // Scan through each, skipping any that fail either of the following tests.
      if  (

          // Don't react to self.
          scanTarget != agent.getActor()

          // Timid actors ignore each other.
          && !(actor.hasFlag(PhysicalFlag.TIMID)
          && scanTarget.hasFlag(PhysicalFlag.TIMID))) {


      // If we've passed the tests, determine if this actor controller is within either visual or
      // auditory range (in that order). If it is, react accordingly, if not, go to the next
      // actor controller in this area.

        final Coordinate targetAt = scanTarget.getCoordinate();

        // Can we see the target?
        if (Perception.getCanSeeLocation(perceptionRank, actor.getFacing(), actorAt, targetAt)) {
          Routines.evaluateOther(agent, scanTarget);
        }

        // Can we hear the target?
        else if (Perception.getCanHearLocation(perceptionRank, actorAt, targetAt)) {
          agent.exhibitBehavior(new Ai_Investigate(agent, targetAt, scanTarget));
        }

      }

    }

  }


  /**
   * Determines how the agent should react to the given other. If either the agent or the other
   * have the aggressive flag, the agent will enter fight or flee. In other words, aggressives
   * will attack/flee everything they encounter, while non-aggressives will only attack/flee
   * aggressives.
   */
  public static void evaluateOther(AiActorAgent agent, Actor other) {

    if (other.hasFlag(PhysicalFlag.AGGRESSIVE)
        || agent.getActor().hasFlag(PhysicalFlag.AGGRESSIVE)) {
      evaluateNewAggressor(agent, other);
    }

  }


  /**
   * Determines how the agent should react to an aggressor upon first encounter. Actors with the
   * timid flag will always retreat on this call. Other actors will call {@code
   * getShouldFleeCombat()} to determine if they should retreat. If they do not retreat, they
   * will begin fighting the aggressor.
   */
  public static void evaluateNewAggressor(AiActorAgent agent, Actor aggressor) {

    final Behavior response;

    if (agent.getActor().hasFlag(PhysicalFlag.TIMID) || getShouldFleeCombat(agent)) {
      response = new Ai_Retreat(agent, aggressor);
    }
    else {
      response = new Ai_Fight(agent, aggressor);
    }

    agent.exhibitBehavior(response);

  }


  /**
   * Determines if the given agent is up for a fight or if it should retreat to nurse its wounds.
   * Actors with the aggressive flag will let their health get lower before fleeing, while actors
   * with the timid flag will do the opposite. Actors with neither flag will retreat somewhere in
   * the middle.
   */
  public static boolean getShouldFleeCombat(AiActorAgent agent) {

    final Actor actor = agent.getActor();
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