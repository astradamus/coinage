package controller.ai;

import actor.Actor;
import actor.attribute.Attribute;
import actor.attribute.Perception;
import actor.attribute.Rank;
import controller.action.Action;
import controller.action.ActionFlag;
import controller.action.Attacking;
import controller.action.Moving;
import game.Game;
import game.TimeMode;
import game.display.Event;
import game.display.EventLog;
import game.physical.PhysicalFlag;
import world.Coordinate;

/**
 * This behavior will make the agent engage in combat with the given victim. The agent will
 * continually attack the victim for as long as it is within range. If the victim moves out of
 * range, the agent will either pursue the target or take the opportunity to escape.<br><br>
 *
 * This choice depends on two factors: the health percentage of the agent, and whether it has
 * the aggressive flag, the timid flag, or neither. Aggressive actors are less likely to flee
 * combat (they will wait until their health is lower), while timid actors are more likely to
 * flee (they will run with much higher health). Actors with neither flag lie right in the middle.
 */
public class Fight extends Behavior {

  private final Actor victim;

  public Fight(AIAgent agent, Actor victim) {
    super(agent);
    this.victim = victim;
  }

  @Override
  protected String getOnExhibitLogMessage() {
    if (victim == Game.getActivePlayerActor()) {
      return getActor().getName() + " doesn't look too friendly.";
    }
    else {
      return null;
    }
  }

  @Override
  protected void onExhibit() {

    fight();

    if (victim == Game.getActivePlayerActor() && Game.getTimeMode() == TimeMode.LIVE) {
      Game.setTimeMode(TimeMode.PRECISION);
      EventLog.registerEvent(Event.INVALID_ACTION,
          "Precision mode has been enabled because you are under attack.");
    }

  }


  private void fight() {

    // If our victim is dead, we can stop attacking them.
    if (victim.hasFlag(PhysicalFlag.DEAD)) {
      markComplete();
    }

    else {

      final Coordinate actorAt = getActor().getCoordinate();
      final Coordinate enemyAt = victim.getCoordinate();

      // If we are adjacent to our enemy, attack them.
      if (actorAt.getIsAdjacentTo(enemyAt)) {
        getActor().attemptAction(new Attacking(getActor(), victim.getCoordinate()));
      }

      else {

        // Consider taking this opportunity to escape.
        if (Routines.getShouldFleeCombat(getAgent())) {
          getAgent().exhibitBehavior(new Retreat(getAgent(), victim));
        }

        else {

          final Rank perception = getActor().getAttributeRank(Attribute.PERCEPTION);

          // If we can track our enemy, pursue them and continue the fight.
          if (Perception.getCanTrackLocation(perception, actorAt, enemyAt)) {
            Routines.approachOneStep(getAgent(), enemyAt);
          }

          // Otherwise, we have lost them and must give up.
          else {
            markComplete();
          }


        }

      }

    }

  }


  @Override
  public void onActorTurnComplete() {

    // If we have no action queued, run the main routine, which will determine whether we should
    // fight, pursue or flee.
    if (getActor().isFreeToAct()) {
      fight();
    }

  }

  @Override
  public void onActionExecuted(Action action) {

    // Moves only fail because of blocked squares, so step around the blockage.
    if (action.hasFlag(ActionFlag.FAILED) && action.getClass() == Moving.class) {
      Routines.stepAroundBlockedSquare(getAgent());
    }

  }

  @Override
  public void onVictimized(Actor attacker) {

    // Each time we are attacked in combat, consider fleeing instead of continuing to fight.
    if (Routines.getShouldFleeCombat(getAgent())) {
      getAgent().exhibitBehavior(new Retreat(getAgent(), victim));
    }

  }

}