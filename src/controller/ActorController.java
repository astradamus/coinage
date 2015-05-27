package controller;

import actor.Actor;
import game.Game;
import world.World;

import java.awt.*;


/**
 *
 */
public abstract class ActorController implements Controller {


  private final Actor actor;
  private final Point globalCoordinate;


  public ActorController(Actor actor, Point globalCoordinate) {

    if (actor == null || globalCoordinate == null) {
      throw new IllegalArgumentException("Cannot instantiate ActorController without an actor and" +
          " a globalCoordinate.");
    }

    this.actor = actor;
    this.globalCoordinate = globalCoordinate;

  }




  private Action action;
  private Direction facing;
  private int beatsToRecover = 0;

  protected final void startMoving(Direction movingIn) {
    action = Action.MOVING;
    facing = movingIn;
  }

  protected final void stopMoving() {
    action = null;
  }

  protected final Action getCurrentAction() {
    return action;
  }


  /**
   * @return A new Point containing this ActorController's globalCoordinate. Changes to this Point
   * are not reflected by this ActorController.
   */
  public final Point getGlobalCoordinate() {
    return new Point(globalCoordinate);
  }





  @Override
  public final void onUpdate() {

    World world = Game.getActive().WORLD;

    if (beatsToRecover > 0) {

      beatsToRecover--; // until this is zero no action can be taken.

    } else if (action == Action.MOVING) {

      int newX = globalCoordinate.x + facing.relativeX;
      int newY = globalCoordinate.y + facing.relativeY;


      if (world.move(actor, globalCoordinate.x, globalCoordinate.y, newX, newY)) {

        globalCoordinate.setLocation(newX, newY);
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

}