package controller;

import actor.Actor;
import game.Game;

import java.awt.*;

/**
 * Simple ActorController that wanders aimlessly, picking random directions in which to walk a
 * random distance, and chaining these walks together to form walks of random length.
 */
public class AnimalController extends ActorController {

  private int wanderChain = -1;
  private int waiting = -1;

  public AnimalController(Actor actor, Point location) {
    super(actor, location);
  }


  private void startWander() {
    startMoving(Direction.values()[Game.RANDOM.nextInt(Direction.values().length)]);
  }

  private void stopWander() {
    stopMoving();
    wanderChain = -1;
    waiting = Game.RANDOM.nextInt(40);
  }





  @Override
  protected void onUpdateProcessed() {

    if (getCurrentAction() == null) {

      if (waiting > 0) {

        waiting--;

      } else {

        // start moving in a random direction
        startWander();
        wanderChain = Game.RANDOM.nextInt(6);

      }

    }

  }

  @Override
  protected void onMoveSucceeded() {
    if (Game.RANDOM.nextDouble() < 0.15) {
      if (wanderChain > 0) {
        wanderChain--;
        startWander();
      } else {
        stopWander();
      }
    }
  }

  @Override
  protected void onMoveFailed() {
    stopWander();
  }

  @Override
  public int getRolledInitiative() {
    return 0;
  }

}
