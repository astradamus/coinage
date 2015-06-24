package controller.ai;

import actor.Actor;
import actor.stats.Health;
import controller.ActorController;
import controller.action.Action;
import controller.action.Moving;
import controller.action.TurnThenMove;
import game.Direction;
import game.physical.PhysicalFlag;
import world.Coordinate;

/**
 *
 */
public class AIRoutines {


  public static void approachOneStep(AIController aiController, Coordinate destination) {

    final Coordinate actorAt = aiController.getActor().getCoordinate();

    final Direction toPursue = Direction.fromPointToPoint(
        actorAt.globalX, actorAt.globalY,
        destination.globalX, destination.globalY);

    final Action action;

    if (aiController.getActor().getFacing() != toPursue) {
      action = new TurnThenMove(aiController, toPursue, false);
    } else {
      action = new Moving(aiController, toPursue, false);
    }

    aiController.attemptAction(action.doNotRepeat());

  }

  public static boolean getShouldFlee(AIController aiController) {
    final Actor actor = aiController.getActor();
    final Health health = actor.getHealth();
    return  (actor.hasFlag(PhysicalFlag.TIMID) || health.getCurrent() / health.getMaximum() < 0.40);
  }

  public static void fightOrFlight(AIController aiController, ActorController attacker) {
    AIBehavior response;

    if (getShouldFlee(aiController)) {
      response = new AI_Retreat(aiController, attacker);
    } else {
      response = new AI_Fight(aiController, attacker);
    }

    aiController.exhibitBehavior(response);
  }


}