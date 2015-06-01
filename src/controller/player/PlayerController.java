package controller.player;

import actor.Actor;
import controller.ActorController;
import game.Game;
import utils.Dimension;

import java.awt.Point;

/**
 * ActorController that enables movement of an Actor with keyboard input.
 */
public class PlayerController extends ActorController {

  private final Component_WorldMapRevealed component_worldMapRevealed;


  public PlayerController(Actor actor, Dimension worldSizeInAreas, Point globalStartLocation) {

    super(actor, globalStartLocation);

    this.component_worldMapRevealed = new Component_WorldMapRevealed(worldSizeInAreas);

  }

  @Override
  protected void onMoveSucceeded() {

    // Update WorldMapRevealed component accordingly.
      Point worldCoord = Game.getActive().WORLD.getWorldCoordinateFromGlobalCoordinate(getGlobalCoordinate());
      component_worldMapRevealed.setAreaIsRevealed(worldCoord.x,worldCoord.y);

  }

  @Override
  public int getRolledInitiative() {
    return 0;
  }


  public final Component_WorldMapRevealed getWorldMapRevealedComponenet() {
    return component_worldMapRevealed;
  }

}
