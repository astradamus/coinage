package controller;

import actor.Actor;
import game.Game;
import world.World;

import java.awt.*;
import java.awt.event.KeyListener;

/**
 * Prototypical ActorController that enables movement of an Actor with keyboard input.
 */
public class PlayerController extends ActorController {

  private Action action;
  private Direction facing;

  private int beatsToRecover = 0;

  private KeyListener listener = new NumPadDirectionListener(this);

  public PlayerController(Actor actor, Point location) {
    super(actor, location);
  }


  void startMoving(Direction movingIn) {
    action = Action.MOVING;
    facing = movingIn;
  }

  void stopMoving() {
    action = null;
  }



  @Override
  public void onUpdate() {
    World world = Game.getActive().WORLD;

    if (beatsToRecover > 0) {

      beatsToRecover--; // until this is zero no action can be taken.

    } else if (action == Action.MOVING) {

      int newX = worldLocation.x + facing.relativeX;
      int newY = worldLocation.y + facing.relativeY;

      if (world.globalMovePhysical(actor, worldLocation.x, worldLocation.y, newX, newY)) {
        worldLocation.setLocation(newX, newY);
        beatsToRecover = action.beatsToPerform;
      }

    }
  }

  @Override
  public int getRolledInitiative() {
    return 0;
  }

  public int getX() {
    return worldLocation.x;
  }
  public int getY() {
    return worldLocation.y;
  }

  public KeyListener getKeyListener() {
    return listener;
  }

}
