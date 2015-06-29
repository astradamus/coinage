package controller.ai;

import actor.Actor;
import actor.attribute.Attribute;
import actor.attribute.Perception;
import actor.attribute.Rank;
import controller.ActorController;
import controller.action.Turning;
import game.Direction;
import game.Game;
import world.Coordinate;

/**
 * This behavior will make the puppet respond to a sound produced by a given intruder. The puppet
 * will turn towards the source of the original sound, performing a limited sensory scan each
 * update that only seeks the intruder who made the sound. This is done instead of performing a
 * regular sensory scan purely to conserve processing power. This could potentially raise weird
 * behavioral issues, where one actor causes an investigation that results in the puppet
 * completely ignoring some other actor that walks up to kill it. However, as it stands now, the
 * odds of this happening are very low, and the odds of it being noticed by the player if it does
 * happen are even lower.
 */
public class AI_Investigate extends AIBehavior {

  private final Coordinate sourceOfSound;
  private final ActorController intruder;

  public AI_Investigate(AIController investigator, Coordinate sourceOfSound,
                        ActorController intruder) {
    super(investigator);
    this.sourceOfSound = sourceOfSound;
    this.intruder = intruder;
  }

  @Override
  protected String getOnExhibitLogMessage() {
    if (intruder == Game.getActivePlayer()) {
      return getPuppet().getActor().getName() + " has heard you.";
    }
    else {
      return null;
    }
  }

  @Override
  protected void onExhibit() {
    investigate();
  }


  private void investigate() {

    final Actor actor = getPuppet().getActor();

    final Rank perception = actor.readAttributeLevel(Attribute.PERCEPTION);
    final Direction actorFacing = actor.getFacing();
    final Coordinate actorAt = actor.getCoordinate();

    final Coordinate intruderActuallyAt = intruder.getActor().getCoordinate();

    // If we can see what we're looking for, react to it.
    if (Perception.getCanSeeLocation(perception, actorFacing, actorAt, intruderActuallyAt)) {
      AIRoutines.evaluateOther(getPuppet(), intruder);
    }

    else {

      final Direction towardsSourceOfSound = Direction.fromPointToPoint(
          actorAt.globalX, actorAt.globalY, sourceOfSound.globalX, sourceOfSound.globalY);

      // If we're already looking at the source of the sound, and we haven't seen anything, we
      // can abandon the search.
      if (actorFacing == towardsSourceOfSound) {
        markComplete();
      }

      // Otherwise, try to turn towards the sound.
      else {
        getPuppet().attemptAction(new Turning(getPuppet(), towardsSourceOfSound));
      }

    }

  }


  @Override
  public void onActorTurnComplete() {

    // Run the main routine at the end of every update.
    investigate();

  }

  @Override
  public void onVictimized(ActorController attacker) {

    // If we are attacked, either fight or flee.
    AIRoutines.evaluateNewAggressor(getPuppet(), attacker);

  }

}