package controller;

import actor.Actor;
import game.Game;
import utils.Dimension;

import java.awt.Point;
import java.awt.event.KeyListener;

/**
 * ActorController that enables movement of an Actor with keyboard input.
 */
public class PlayerController extends ActorController {

  private final Component_WorldMapRevealed component_worldMapRevealed;
  private final KeyListener listener;


  public PlayerController(Actor actor, Dimension worldSizeInAreas, Point globalStartLocation) {
    super(actor, globalStartLocation);

    this.component_worldMapRevealed = new Component_WorldMapRevealed(worldSizeInAreas);
    listener = new NumPadDirectionListener(this);

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

  public KeyListener getKeyListener() {
    return listener;
  }

}
