package controller.ai;

import actor.Actor;
import actor.attribute.Attribute;
import actor.attribute.Perception;
import actor.attribute.Rank;
import controller.ActorController;
import controller.action.Turning;
import game.Direction;
import game.display.EventLog;
import world.Coordinate;

import java.awt.Color;

/**
 *
 */
public class AI_Investigate extends AIBehavior {

  private final Coordinate lookingAt;
  private final ActorController lookingFor;

  public AI_Investigate(AIController investigator, Coordinate lookingAt,
                        ActorController lookingFor) {
    super(investigator);
    this.lookingAt = lookingAt;
    this.lookingFor = lookingFor;

    investigate();
  }

  private void investigate() {

    final Actor actor = getPuppet().getActor();

    final Rank perception = actor.readAttributeLevel(Attribute.PERCEPTION);
    final Direction actorFacing = actor.getFacing();
    final Coordinate actorAt = actor.getCoordinate();

    final Coordinate whereTargetIsNow = lookingFor.getActor().getCoordinate();


    // If we can see what we're looking for, react to it.
    if (Perception.getCanSeeLocation(perception, actorFacing, actorAt, whereTargetIsNow)) {
      AIRoutines.evaluateThreat(getPuppet(), lookingFor);
      return;
    }

    // Otherwise, try to turn towards the sound we heard--or give up if we already have.
    final Direction towardsLookingAt = Direction.fromPointToPoint(
        actorAt.globalX, actorAt.globalY,
        lookingAt.globalX, lookingAt.globalY);

    if (actorFacing == towardsLookingAt) {
      markComplete();
    } else {
      getPuppet().attemptAction(new Turning(getPuppet(), towardsLookingAt));
    }

  }

  @Override
  public void onActorTurnComplete() {
    investigate();
  }


  @Override
  public void onVictimized(ActorController attacker) {
    AIRoutines.fightOrFlee(getPuppet(), attacker);
  }

}