package controller;

import actor.Actor;
import game.Game;
import world.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

/**
 * Prototypical ActorController that enables movement of an Actor with keyboard input.
 */
public class PlayerController extends ActorController {

  Action action;
  Stack<Direction> movingDirections = new Stack<>();
  int beatsToRecover = 0;

  KeyListener keyListener = new KeyListener() {

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      Direction direction = Direction.fromKeyEvent(e);
      if (direction != null) {
        if (movingDirections.isEmpty()) {
          action = Action.MOVING;
        }
        if (!movingDirections.contains(direction)){
          movingDirections.push(direction);
        }
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      Direction direction = Direction.fromKeyEvent(e);
      movingDirections.remove(direction);
    }

  };

  public PlayerController(Actor actor, Point location) {
    super(actor, location);
  }

  @Override
  public void onUpdate() {
    World world = Game.getActive().WORLD;

    if (beatsToRecover > 0) {
        beatsToRecover--;
    } else {
      if (action == Action.MOVING) {
        if (movingDirections.isEmpty()) {
          action = null;
        } else {
          Direction dir = movingDirections.peek();
          int newX = location.x + dir.relativeX;
          int newY = location.y + dir.relativeY;
          if (world.movePhysical(actor, location.x, location.y, newX, newY)) {
            location.setLocation(newX, newY);
            beatsToRecover = action.beatsToPerform;
          }
        }
      }
    }
  }

  @Override
  public int getRolledInitiative() {
    return 0;
  }

  public KeyListener getKeyListener() {
    return keyListener;
  }

}
