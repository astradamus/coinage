package controller.ai;

import controller.action.Action;
import controller.action.Moving;
import controller.action.TurnThenMove;
import game.Direction;
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


}