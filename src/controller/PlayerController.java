package controller;

import actor.Actor;
import game.Game;
import utils.Dimension;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * ActorController that enables movement of an Actor with keyboard input.
 */
public class PlayerController extends ActorController implements DirectionListener {

  private final Component_WorldMapRevealed component_worldMapRevealed;
  private final List<KeyListener> keyListeners;


  public PlayerController(Actor actor, Dimension worldSizeInAreas, Point globalStartLocation) {

    super(actor, globalStartLocation);

    this.component_worldMapRevealed = new Component_WorldMapRevealed(worldSizeInAreas);
    keyListeners = new ArrayList<>();
    keyListeners.add(new NumPadDirectionInterpreter(this));

  }


  @Override
  public void receiveDirection(Direction direction) {
    startMoving(direction);
  }

  @Override
  public void receiveDirectionsCleared() {
    stopMoving();
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

  public List<KeyListener> getKeyListeners() {
    return new ArrayList<>(keyListeners);
  }

}
