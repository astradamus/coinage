package controller.player;

import actor.Actor;
import controller.action.Action;
import controller.ActorController;
import controller.action.ActionFlag;
import utils.Dimension;

/**
 * ActorController that enables movement of an Actor with keyboard input.
 */
public class PlayerController extends ActorController {

  private final Component_WorldMapRevealed component_worldMapRevealed;


  public PlayerController(Actor actor, Dimension worldSizeInAreas) {

    super(actor);

    this.component_worldMapRevealed = new Component_WorldMapRevealed(worldSizeInAreas);

  }

  @Override
  public void onActionExecuted(Action action) {

    // Update WorldMapRevealed component accordingly.
    if (action.hasFlag(ActionFlag.SUCCEEDED) && action.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {
      component_worldMapRevealed.setAreaIsRevealed(getActor().getCoordinate());
    }

  }


  public final Component_WorldMapRevealed getWorldMapRevealedComponent() {
    return component_worldMapRevealed;
  }

}
