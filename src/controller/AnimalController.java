package controller;

import actor.Actor;
import game.Game;
import world.Area;
import world.World;

import java.awt.*;

/**
 * A very basic, prototypical ActorController that will wander about within the confines of the
 * map, taking breaks of variable length in which it just stands about. Tends to gravitate
 * towards and wait longer beside trees.
 */
public class AnimalController extends ActorController {

  private int waiting = -1;
  private final Point wandering = new Point(-1,-1);

  public AnimalController(Actor actor, Point location) {
    super(actor, location);
  }


  @Override
  public void onUpdate() {
    World world = Game.getActive().WORLD;

    if (wandering.x == -1) {

      if (waiting < 0) {


        // Pick a non-blocked worldLocation to wander to.
        do {
          wandering.x = Game.RANDOM.nextInt(world.globalWidth);
          wandering.y = Game.RANDOM.nextInt(world.globalHeight);
        } while (world.globalIsBlocked(wandering.x, wandering.y));

      } else {
        waiting--;
      }

    } else {

      if (worldLocation.equals(wandering)) {
        stopWander();
      } else {

        int newX = worldLocation.x;
        int newY = worldLocation.y;

        if (Game.RANDOM.nextBoolean()) {

          if (wandering.x > worldLocation.x) {
            newX += 1;
          } else
          if (wandering.x < worldLocation.x) {
            newX -= 1;
          }

        } else {


          if (wandering.y > worldLocation.y) {
            newY += 1;
          } else
          if (wandering.y < worldLocation.y) {
            newY -= 1;
          }

        }

        if (world.globalMovePhysical(actor, worldLocation.x, worldLocation.y, newX, newY)) {
          worldLocation.setLocation(newX,newY);
        } else {
          stopWander();
        }

      }

    }
  }

  private void stopWander() {
    wandering.setLocation(-1,-1);
    waiting = Game.RANDOM.nextInt(40);
  }

  @Override
  public int getRolledInitiative() {
    return 0;
  }
}
