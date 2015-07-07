package controller.ai;

import actor.Actor;
import actor.attribute.Attribute;
import actor.attribute.Perception;
import actor.attribute.Rank;
import controller.action.Turning;
import game.Direction;
import world.Coordinate;

/**
 * This behavior will make the agent respond to a sound produced by a given intruder. The agent
 * will turn towards the source of the original sound, performing a limited sensory scan each
 * update that only seeks the intruder who made the sound. This is done instead of performing a
 * regular sensory scan purely to conserve processing power. This could potentially raise weird
 * behavioral issues, where one actor causes an investigation that results in the agent
 * completely ignoring some other actor that walks up to kill it. However, as it stands now, the
 * odds of this happening are very low, and the odds of it being noticed by the player if it does
 * happen are even lower.
 */
public class Ai_Investigate extends Behavior {

  private final Coordinate sourceOfSound;
  private final Actor intruder;

  public Ai_Investigate(AiActorAgent investigator, Coordinate sourceOfSound,
                        Actor intruder) {
    super(investigator);
    this.sourceOfSound = sourceOfSound;
    this.intruder = intruder;
  }

  @Override
  protected String getOnExhibitLogMessage() {
    if (getAgent().getGameInformer().getActorIsPlayer(intruder)) {
      return getActor().getName() + " has heard you.";
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

    final Rank perception = getActor().getAttributeRank(Attribute.PERCEPTION);
    final Direction actorFacing = getActor().getFacing();
    final Coordinate actorAt = getActor().getCoordinate();

    final Coordinate intruderActuallyAt = intruder.getCoordinate();

    // If we can see what we're looking for, react to it.
    if (Perception.getCanSeeLocation(perception, actorFacing, actorAt, intruderActuallyAt)) {
      Routines.evaluateOther(getAgent(), intruder);
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
        getAgent().attemptAction(new Turning(getActor(), towardsSourceOfSound));
      }

    }

  }


  @Override
  public void onActorTurnComplete() {

    // Run the main routine at the end of every update.
    investigate();

  }

  @Override
  public void onVictimized(Actor attacker) {

    // If we are attacked, either fight or flee.
    Routines.evaluateNewAggressor(getAgent(), attacker);

  }

}