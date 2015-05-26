package controller;

import actor.Actor;
import game.Game;
import world.Area;
import world.World;

import java.awt.*;
import java.awt.event.KeyListener;

/**
 * Prototypical ActorController that enables movement of an Actor with keyboard input.
 */
public class PlayerController extends ActorController {

  private boolean[][] worldMapExplored;

  private Action action;
  private Direction facing;

  private int beatsToRecover = 0;

  private KeyListener listener = new NumPadDirectionListener(this);

  public PlayerController(Actor actor, Point location) {
    super(actor, location);
    World world = Game.getActive().WORLD;
    worldMapExplored = new boolean[world.worldAreasTall][world.worldAreasWide];
    worldMapExplored[getY()/world.areaHeight][getX()/world.areaWidth] = true;
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

        // Reveal current area on World Map
        worldMapExplored[newY/world.areaHeight][newX/world.areaWidth] = true;

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

  public boolean getWorldMapAreaExplored(int worldX, int worldY) {
    return worldMapExplored[worldY][worldX];
  }
}
