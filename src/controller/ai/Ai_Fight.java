package controller.ai;

import actor.Actor;
import actor.action.Action;
import actor.action.ActionFlag;
import actor.action.Attacking;
import actor.action.Moving;
import actor.attribute.Attribute;
import actor.attribute.Perception;
import actor.attribute.Rank;
import game.TimeMode;
import game.io.GameEngine;
import game.io.display.Event;
import game.io.display.EventLog;
import game.physical.PhysicalFlag;
import world.Coordinate;
import world.MapCoordinate;
import world.World;

/**
 * This behavior will make the agent engage in combat with the given victim. The agent will
 * continually attack the victim for as long as it is within range. If the victim moves out of
 * range, the agent will either pursue the target or take the opportunity to escape.
 * <p>
 * This choice depends on two factors: the health percentage of the agent, and whether it has the
 * aggressive flag, the timid flag, or neither. Aggressive actors are less likely to flee combat
 * (they will wait until their health is lower), while timid actors are more likely to flee (they
 * will run with much higher health). Actors with neither flag lie right in the middle.
 */
public class Ai_Fight extends Behavior {

  private final Actor victim;


  public Ai_Fight(AiActorAgent agent, Actor victim) {
    super(agent);
    this.victim = victim;
  }


  @Override
  protected String getOnExhibitLogMessage() {
    if (getAgent().getGameInformer().getActorIsPlayer(victim)) {
      return getActor().getName() + " isn't looking too friendly.";
    }
    else {
      return null;
    }
  }


  @Override
  protected void onExhibit() {

    fight();

    if (getAgent().getGameInformer().getActorIsPlayer(victim)
        && GameEngine.getTimeMode() == TimeMode.LIVE) {
      GameEngine.setTimeMode(TimeMode.PRECISION);
      EventLog.registerEvent(Event.ALERT_MAJOR,
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
        final Attacking action = new Attacking(getActor(), victim.getCoordinate());

        if (getAgent().getGameInformer().getActorIsPlayer(victim)) {
          action.playerIsTarget();
        }

        getAgent().attemptAction(action);
      }

      else {

        // Consider taking this opportunity to escape.
        if (Routines.getShouldFleeCombat(getAgent())) {
          getAgent().exhibitBehavior(new Ai_Retreat(getAgent(), victim));
        }

        else {

          final World.Informer worldInformer = getAgent().getGameInformer().getWorldInformer();

          final Rank perception = getActor().getAttributeComponent().getRank(Attribute.PERCEPTION);
          final MapCoordinate actorMC = worldInformer.convertToMapCoordinate(actorAt);
          final MapCoordinate enemyMC = worldInformer.convertToMapCoordinate(enemyAt);

          // If we can track our enemy, pursue them and continue the fight.
          if (Perception.getCanTrackLocation(perception, actorMC, enemyMC)) {
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
  public void onActionExecuted(Action action) {

    // Moves only fail because of blocked squares, so step around the blockage.
    if (action.hasFlag(ActionFlag.FAILED) && action.getClass() == Moving.class) {
      Routines.stepAroundBlockedSquare(getAgent());
    }
  }


  @Override
  public void onActorTurnComplete() {

    // If we have no action queued, run the main routine, which will determine whether we should
    // fight, pursue or flee.
    if (getActor().getActionComponent().isFreeToAct()) {
      fight();
    }
  }


  @Override
  public void onVictimized(Actor attacker) {

    // Each time we are attacked in combat, consider fleeing instead of continuing to fight.
    if (Routines.getShouldFleeCombat(getAgent())) {
      getAgent().exhibitBehavior(new Ai_Retreat(getAgent(), victim));
    }
  }
}