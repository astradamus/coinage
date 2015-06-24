package controller.ai;

import controller.ActorController;
import controller.action.Action;
import controller.action.ActionFlag;
import controller.action.TurnThenMove;
import game.Direction;
import game.Game;
import world.Coordinate;

/**
 *
 */
public class AI_Retreat extends AIBehavior {

  public static final int ADDITIONAL_ATTACKS_BEFORE_FIGHT = 1;


  private final ActorController attacker;

  private int additionalAttacksSuffered;

  public AI_Retreat(AIController retreater, ActorController attacker) {
    super(retreater);
    this.attacker = attacker;
    additionalAttacksSuffered = 0;

    retreat();
  }

  private void retreat() {

    final Coordinate actorAt = getPuppet().getActor().getCoordinate();
    final Coordinate attackerAt = attacker.getActor().getCoordinate();


    final Direction toEscape = Direction.fromPointToPoint(
        attackerAt.globalX, attackerAt.globalY,
        actorAt.globalX, actorAt.globalY);

    getPuppet().attemptAction(new TurnThenMove(getPuppet(), toEscape, false));

  }

  @Override
  public void onActionExecuted(Action action) {

    if (action.hasFlag(ActionFlag.FAILED)) {

      Direction adjust = getPuppet().getActor().getFacing();
      adjust = (Game.RANDOM.nextBoolean()) ? adjust.getLeftNeighbor() : adjust.getRightNeighbor();

      getPuppet().attemptAction(new TurnThenMove(getPuppet(), adjust, false).doNotRepeat());

    }

  }

  @Override
  public void onActorTurnComplete() {

    if (getPuppet().isFreeToAct()) {

      final Coordinate actorAt = getPuppet().getActor().getCoordinate();
      final Coordinate attackerAt = attacker.getActor().getCoordinate();

      final int deltaX = actorAt.worldX-attackerAt.worldX;
      final int deltaY = actorAt.worldY-attackerAt.worldY;

      final boolean attackerIsTwoAreasAway = deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1;

      if (attackerIsTwoAreasAway) {
        markComplete();
        return;
      }

      retreat();

    }

  }

  @Override
  public void onVictimized(ActorController attacker) {
    if (this.attacker == attacker) {
      additionalAttacksSuffered++;

      if (additionalAttacksSuffered >= ADDITIONAL_ATTACKS_BEFORE_FIGHT) {
        getPuppet().exhibitBehavior(new AI_Fight(getPuppet(), attacker));
      }

    }
  }


}
