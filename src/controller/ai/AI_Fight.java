package controller.ai;

import controller.ActorController;
import controller.action.Action;
import controller.action.ActionFlag;
import controller.action.Attacking;
import controller.action.Moving;
import controller.action.TurnThenMove;
import game.Direction;
import game.Game;
import game.physical.PhysicalFlag;
import utils.Utils;
import world.Coordinate;

/**
 *
 */
public class AI_Fight extends AIBehavior {

  private final ActorController victim;

  public AI_Fight(AIController fighter, ActorController victim) {
    super(fighter);
    this.victim = victim;

    fight();
  }


  private void fight() {
    if (victim.getActor().hasFlag(PhysicalFlag.DEAD)) {
      markComplete();
      return;
    }

    final Coordinate actorAt = getPuppet().getActor().getCoordinate();
    final Coordinate enemyAt = victim.getActor().getCoordinate();

    final boolean actorsAdjacent = Utils.getPointsAreAdjacent(
        actorAt.globalX, actorAt.globalY,
        enemyAt.globalX, enemyAt.globalY);

    if (actorsAdjacent) {

      // if adjacent, attack
      getPuppet().attemptAction(new Attacking(getPuppet(), victim.getActor().getCoordinate(), victim));

    } else {

      // if distant, pursue
      AIRoutines.approachOneStep(getPuppet(), enemyAt);

    }

  }

  @Override
  public void onActionExecuted(Action action) {

    if (action.hasFlag(ActionFlag.FAILED) && action.getClass() == Moving.class) {

      Direction turningTowards = getPuppet().getActor().getFacing();
      if (Game.RANDOM.nextBoolean()) {
        turningTowards = turningTowards.getLeftNeighbor();
      } else {
        turningTowards = turningTowards.getRightNeighbor();
      }

      getPuppet().attemptAction(new TurnThenMove(getPuppet(), turningTowards, false).doNotRepeat());

    }

  }

  @Override
  public void onActorTurnComplete() {

    if (getPuppet().isFreeToAct()) {
      fight();
    }

  }

}
