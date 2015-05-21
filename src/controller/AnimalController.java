package controller;

import actor.Actor;
import game.Game;
import world.Area;

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
    Area area = Game.getActive().AREA;
    if (wandering.x == -1) {

      if (waiting < 0) {

        // Pick a non-blocked location to wander to.
        do {
          wandering.x = Game.RANDOM.nextInt(area.getWidth());
          wandering.y = Game.RANDOM.nextInt(area.getHeight());
        } while (area.isBlocked(wandering.x, wandering.y));

      } else {
        waiting--;
      }

    } else {

      if (location.equals(wandering)) {
        stopWander();
      } else {

        int newX = location.x;
        int newY = location.y;

        if (Game.RANDOM.nextBoolean()) {

          if (wandering.x > location.x) {
            newX += 1;
          } else
          if (wandering.x < location.x) {
            newX -= 1;
          }

        } else {


          if (wandering.y > location.y) {
            newY += 1;
          } else
          if (wandering.y < location.y) {
            newY -= 1;
          }

        }

        if (area.movePhysical(actor, location.x, location.y, newX, newY)) {
          location.setLocation(newX,newY);
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
