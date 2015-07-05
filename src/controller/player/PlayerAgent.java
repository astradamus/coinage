package controller.player;

import actor.Actor;
import controller.ActorAgent;
import controller.action.Action;
import controller.action.ActionFlag;
import game.Game;
import world.Coordinate;
import world.World;
import world.MapCoordinate;

/**
 * ActorAgent that enables movement of an Actor with keyboard input.
 */
public class PlayerAgent extends ActorAgent {

  private final World world;
  private final Component_WorldMapRevealed component_worldMapRevealed;

  public PlayerAgent(Actor actor, World world) {
    super(actor);
    this.component_worldMapRevealed = new Component_WorldMapRevealed(world.getWorldSizeInAreas());
    this.world = world;
  }

  @Override
  public void onActionExecuted(Action action) {

    // Update WorldMapRevealed component accordingly.
    if (action.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {

      MapCoordinate playerAt = world.convertToMapCoordinate(getActor().getCoordinate());
      component_worldMapRevealed.setAreaIsRevealed(playerAt);
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