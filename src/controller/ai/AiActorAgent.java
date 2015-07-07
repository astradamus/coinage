package controller.ai;

import actor.Actor;
import controller.ActorAgent;
import controller.action.Action;
import controller.action.ActionFlag;
import game.Game;
import game.io.display.Event;
import game.io.display.EventLog;
import world.Area;
import world.World;

import java.util.Set;

/**
 * This actor controller uses modular {@code Behavior} packages to give non-player-characters
 * behavior. It hands off many of its method calls to whichever behavior it is currently
 * exhibiting, so that the interpretation of that call can vary situationally. One package can
 * freely call for the controller to switch to another. All packages can flag themselves with
 * {@code markComplete()} to tell the controller it can safely return to an idle state.
 */
public class AiActorAgent extends ActorAgent {

  private Game.Reporter gameReporter;
  private Behavior currentBehavior;

  public AiActorAgent(Actor actor, Game.Reporter gameReporter) {
    super(actor);
    this.gameReporter = gameReporter;
    currentBehavior = null;
  }

  /**
   * Changes the current Behavior package to the one specified and starts the routine. If there
   * is a relevant event log message to be printed, do so.
   */
  void exhibitBehavior(Behavior Behavior) {

    currentBehavior = Behavior;

    final String onExhibitLogMessage = currentBehavior.getOnExhibitLogMessage();

    if (onExhibitLogMessage != null) {
      EventLog.registerEvent(Event.OTHER_ACTOR_ACTIONS, onExhibitLogMessage);
    }

    currentBehavior.onExhibit();

  }

  @Override
  protected void onActorObserverDisconnected() {
    currentBehavior = null;
  }

  @Override
  public void onActionExecuted(Action action) {

    if (action.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {
      final World world = gameReporter.getWorld();
      Area from = world.getArea(action.getOrigin());
      Area to = world.getArea(getActor().getCoordinate());
      getControllerInterface().onLocalityChanged(this, from, to);
    }

    // Pass the call to our current behavior, if we have one.
    if (currentBehavior != null) {
      currentBehavior.onActionExecuted(action);
    }

  }

  @Override
  public void onActorTurnComplete() {

    // If we have no behavior or our current behavior is finished, enter a simple idling state
    // or, more rarely, start wandering about.
    if (currentBehavior == null || currentBehavior.getIsComplete()) {
      if (Game.RANDOM.nextInt(10) < 1) {
        currentBehavior = new Ai_Wander(this);
      } else {
        currentBehavior = new Ai_Idle(this);
      }
    }

    // Pass the call to our current behavior.
    currentBehavior.onActorTurnComplete();

  }

  @Override
  public void onVictimized(Actor attacker) {

    // Pass the call to our current behavior, if we have one.
    if (currentBehavior != null) {
      currentBehavior.onVictimized(attacker);
    }

  }

  Game.Reporter getGameReporter() {
    return gameReporter;
  }

  @Override
  public Area getLocality(World world) {
    return world.getArea(getActor().getCoordinate());
  }

  Set<Actor> requestActorsInMyArea() {
    return getControllerInterface().requestActorsInMyArea(this);
  }

}