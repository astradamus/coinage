package controller.player;

import actor.Actor;
import controller.ActorAgent;
import controller.action.Action;
import controller.action.ActionFlag;
import world.Area;
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
  public void attemptAction(Action action) {
    getActor().startAction(action.playerIsActor());
  }

  @Override
  public void onActionExecuted(Action action) {

    // Update WorldMapRevealed component accordingly.
    if (action.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {

      MapCoordinate playerAt = world.convertToMapCoordinate(getActor().getCoordinate());
      component_worldMapRevealed.setAreaIsRevealed(playerAt);
      getControllerInterface().reevaluateActiveAreas();

    }

  }


  public final Component_WorldMapRevealed getWorldMapRevealedComponent() {
    return component_worldMapRevealed;
  }

  @Override
  public Area getLocality(World world) {
    return null;  // PlayerAgents are non-local.
  }

}