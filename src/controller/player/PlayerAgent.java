package controller.player;

import actor.Actor;
import controller.ActorAgent;
import controller.action.Action;
import controller.action.ActionFlag;
import game.Game;
import utils.Dimension;
import world.Coordinate;

/**
 * ActorAgent that enables movement of an Actor with keyboard input.
 */
public class PlayerAgent extends ActorAgent {

  private final Component_WorldMapRevealed component_worldMapRevealed;

  public PlayerAgent(Actor actor, Dimension worldSizeInAreas) {
    super(actor);
    this.component_worldMapRevealed = new Component_WorldMapRevealed(worldSizeInAreas);
  }

  @Override
  public void onActionExecuted(Action action) {

    // Update WorldMapRevealed component accordingly.
    if (action.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {
      component_worldMapRevealed.setAreaIsRevealed(getActor().getCoordinate());
      Game.getActiveControllers().onPlayerChangedArea();
    }

  }


  public final Component_WorldMapRevealed getWorldMapRevealedComponent() {
    return component_worldMapRevealed;
  }

  @Override
  public Coordinate getLocality() {
    return null;  // PlayerAgents are non-local.
  }

}