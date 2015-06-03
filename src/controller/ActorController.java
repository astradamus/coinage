package controller;

import actor.Actor;
import game.Direction;
import game.Game;
import world.Coordinate;
import world.World;

/**
 *
 */
public abstract class ActorController implements Controller {

  private final Actor actor;
  private Coordinate coordinate;


  public ActorController(Actor actor, Coordinate coordinate) {

    if (actor == null || coordinate == null) {
      throw new IllegalArgumentException("Cannot instantiate ActorController without an actor and" +
          " a coordinate.");
    }

    this.actor = actor;
    this.coordinate = coordinate;

  }




  private Action action;
  private Direction facing;
  private int beatsToRecover = 0;

  public final void startMoving(Direction movingIn) {
    action = Action.MOVING;
    facing = movingIn;
  }

  public final void stopMoving() {
    action = null;
  }

  protected final Action getCurrentAction() {
    return action;
  }


  /**
   * @return A new Point containing this ActorController's coordinate. Changes to this Point
   * are not reflected by this ActorController.
   */
  public final Coordinate getCoordinate() {
    return coordinate;
  }





  @Override
  public final void onUpdate() {

    World world = Game.getActiveWorld();

    if (beatsToRecover > 0) {

      beatsToRecover--; // until this is zero no action can be taken.

    } else if (action == Action.MOVING) {

      Coordinate newCoordinate =
          world.offsetCoordinateBySquares(coordinate, facing.relativeX, facing.relativeY);

      if (newCoordinate != null && world.move(actor, coordinate, newCoordinate)) {

        coordinate = newCoordinate;
        beatsToRecover = action.beatsToPerform;

        onMoveSucceeded();

      } else {

        onMoveFailed();

      }

    }

    onUpdateProcessed();

  }

  /**
   * Subclasses of ActorController should override this method to hook into successful movement.
   */
  protected void onMoveSucceeded() { }

  /**
   * Subclasses of ActorController should override this method to hook into failed movement,
   * typically caused by colliding with a tree or some other blocking Physical.
   */
  protected void onMoveFailed() { }

  /**
   * Subclasses of ActorController should override this method to hook into game updates. This
   * function is run at the end of this ActorController's onUpdate() call, and should be used to
   * setup the actor for FUTURE updates, rather than the current one.
   */
  protected void onUpdateProcessed() { }

  public Actor getActor() {
    return actor;
  }

}