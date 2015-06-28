package controller.ai;

import controller.ActorController;
import controller.action.Action;
import controller.action.ActionFlag;
import controller.action.Attacking;
import controller.action.Moving;
import controller.action.TurnThenMove;
import game.Direction;
import game.Game;
import game.display.Event;
import game.display.EventLog;
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

    if (victim == Game.getActivePlayer()) {
      EventLog.registerEvent(Event.OTHER_ACTOR_ACTIONS,
          getPuppet().getActor().getName() + " doesn't look too friendly.");
    }

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

      // If adjacent, attack.
      getPuppet().attemptAction(new Attacking(getPuppet(), victim.getActor().getCoordinate()));

    } else {

      // If distant, flee (if timid) or pursue.
      if (AIRoutines.getShouldFleeCombat(getPuppet())) {
        getPuppet().exhibitBehavior(new AI_Retreat(getPuppet(), victim));
      } else {
        AIRoutines.approachOneStep(getPuppet(), enemyAt);
      }

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

  @Override
  public void onVictimized(ActorController attacker) {
    if (AIRoutines.getShouldFleeCombat(getPuppet())) {
      getPuppet().exhibitBehavior(new AI_Retreat(getPuppet(), victim));
    }
  }

}