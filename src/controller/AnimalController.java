package controller;

import controller.action.Action;
import actor.Actor;
import controller.action.ActionFlag;
import controller.action.Moving;
import game.Direction;
import game.Game;

/**
 * Simple ActorController that wanders aimlessly, picking random directions in which to walk a
 * random distance, and chaining these walks together to form walks of random length.
 */
public class AnimalController extends ActorController {

  private int wanderChain = -1;
  private int waiting = -1;

  public AnimalController(Actor actor) {
    super(actor);
  }


  private void startWander() {
    Direction direction = Direction.values()[Game.RANDOM.nextInt(Direction.values().length)];
    attemptAction(new Moving(getActor(), direction));
  }

  private void stopWander() {
    attemptAction(null);
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
  public void onActionExecuted(Action action) {

    if (action.getClass() == Moving.class) {

      if (action.hasFlag(ActionFlag.SUCCEEDED)) {

        if (Game.RANDOM.nextDouble() < 0.15) {
          if (wanderChain > 0) {
            wanderChain--;
            startWander();
          } else {
            stopWander();
          }
        }

      }
      else if (action.hasFlag(ActionFlag.FAILED)) {
        stopWander();
      }

    }

  }

}
