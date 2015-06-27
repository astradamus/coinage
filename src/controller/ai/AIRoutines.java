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
import controller.action.Turning;
import game.Direction;
import game.Game;
import game.display.EventLog;
import game.physical.PhysicalFlag;
import world.Area;
import world.Coordinate;

import java.awt.Color;
import java.util.Set;

/**
 *
 */
public class AIRoutines {


  public static void performSensoryScan(AIController puppet) {

    final Area area = puppet.getLocality();

    final Actor actor = puppet.getActor();
    final Coordinate actorAt = actor.getCoordinate();
    final Rank perceptionRank = actor.readAttributeLevel(Attribute.PERCEPTION);


    Set<ActorController> actorControllers = Game.getActiveControllers().getActorControllersInArea(area);

    for (ActorController scanTarget : actorControllers) {

      final Actor targetActor = scanTarget.getActor();

      if (scanTarget == puppet
          || (actor.hasFlag(PhysicalFlag.TIMID) && targetActor.hasFlag(PhysicalFlag.TIMID))) {
        continue; // Won't react to self, and timid actors ignore each other.
      }

      final Coordinate targetAt = targetActor.getCoordinate();

      // Can we see the target?
      if (Perception.getCanSeeLocation(perceptionRank, actor.getFacing(), actorAt, targetAt)) {
        AIRoutines.evaluateThreat(puppet, scanTarget);
        EventLog.registerEventIfPlayerIsNear(actorAt, Color.ORANGE, actor.getName() + " sees " +
            targetActor.getName());
      }

      // Can we hear the target?
      else if (Perception.getCanHearLocation(perceptionRank, actorAt, targetAt)) {
        puppet.exhibitBehavior(new AI_Investigate(puppet, targetAt, scanTarget));
        EventLog.registerEventIfPlayerIsNear(actorAt, Color.ORANGE, actor.getName() + " hears " +
            targetActor.getName());
      }

    }

  }




  public static void approachOneStep(AIController puppet, Coordinate destination) {

    final Coordinate actorAt = puppet.getActor().getCoordinate();

    final Direction toPursue = Direction.fromPointToPoint(
        actorAt.globalX, actorAt.globalY,
        destination.globalX, destination.globalY);

    final Action action;

    if (puppet.getActor().getFacing() != toPursue) {
      action = new TurnThenMove(puppet, toPursue, false);
    } else {
      action = new Moving(puppet, toPursue, false);
    }

    puppet.attemptAction(action.doNotRepeat());

  }

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

  public static void fightOrFlee(AIController puppet, ActorController attacker) {
    AIBehavior response;

    if (puppet.getActor().hasFlag(PhysicalFlag.TIMID) || getShouldFleeCombat(puppet)) {
      response = new AI_Retreat(puppet, attacker);
    } else {
      response = new AI_Fight(puppet, attacker);
    }

    puppet.exhibitBehavior(response);
  }


  public static void evaluateThreat(AIController puppet, ActorController potentialThreat) {

    // TODO Perhaps a sizing up? Compare own Muscle/Grit to target's Muscle/Grit?
    if (potentialThreat.getActor().hasFlag(PhysicalFlag.AGGRESSIVE)
        || puppet.getActor().hasFlag(PhysicalFlag.AGGRESSIVE)) {
      fightOrFlee(puppet, potentialThreat);
    }

  }

}