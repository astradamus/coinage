package controller.player;

import actor.Actor;
import controller.Action;
import controller.ActorController;
import game.display.Event;
import game.display.EventLog;
import utils.Dimension;
import world.Coordinate;

/**
 * ActorController that enables movement of an Actor with keyboard input.
 */
public class PlayerController extends ActorController {

  private final Component_WorldMapRevealed component_worldMapRevealed;


  public PlayerController(Actor actor, Dimension worldSizeInAreas, Coordinate coordinate) {

    super(actor, coordinate);

    this.component_worldMapRevealed = new Component_WorldMapRevealed(worldSizeInAreas);

  }

  @Override
  protected void onMoveSucceeded() {

    // Update WorldMapRevealed component accordingly.
      component_worldMapRevealed.setAreaIsRevealed(getCoordinate());

  }

  @Override
  protected void onActionFailed(Action action, String message) {
    if (action == Action.MOVING) {
      return; // failed move messages are too spammy and not really necessary.
    }
    EventLog.registerEvent(Event.INVALID_ACTION, message);
  }

  @Override
  public int getRolledInitiative() {
    return 0;
  }


  public final Component_WorldMapRevealed getWorldMapRevealedComponent() {
    return component_worldMapRevealed;
  }

}
