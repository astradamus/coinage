package game;

import controller.Direction;
import controller.DirectionListener;
import controller.NumPadDirectionInterpreter;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameInputSwitch implements DirectionListener, ModeHotkeyListener {

  private final List<KeyListener> keyListeners;

  private final Point cursorTarget = new Point(-1,-1);
  private Direction cursorMovingIn = null;

  private GameMode mode = GameMode.EXPLORE;


  public GameInputSwitch() {

    keyListeners = new ArrayList<>();
    keyListeners.add(new NumPadDirectionInterpreter(this));
    keyListeners.add(new ModeHotkeyInterpreter(this));

  }



  @Override
  public void receiveMode(GameMode mode) {
    if (this.mode != mode) {

      if (mode == GameMode.EXPLORE) {

        cursorTarget.setLocation(-1,-1);
        cursorMovingIn = null;
        this.mode = mode;
        System.out.println(mode.name());
        Game.unpauseGame();

      } else
      if (mode == GameMode.LOOK) {

        Point globalCoordinate =
            Game.getActive().CONTROLLERS.getPlayerController().getGlobalCoordinate();
        Point localCoordinate =
            Game.getActive().WORLD.getAreaCoordinateFromGlobalCoordinate(globalCoordinate);

        cursorTarget.setLocation(localCoordinate);
        this.mode = mode;
        System.out.println(mode.name());
        Game.pauseGame();

      }

    }
  }




  private int moveDelay = 0;
  public void onUpdate() {

    if (mode == GameMode.LOOK && cursorMovingIn != null) {

      if (moveDelay > 0) {
        moveDelay--;
      } else {

        int newX = cursorTarget.x + cursorMovingIn.relativeX;
        int newY = cursorTarget.y + cursorMovingIn.relativeY;

        if (Game.getActive().WORLD.getAreaSizeInSquares().getCoordinateIsWithinBounds(newX, newY)) {
          cursorTarget.setLocation(newX, newY);
          moveDelay = 2;
        }

      }

    }

  }


  @Override
  public void receiveDirection(Direction direction) {

    if (mode == GameMode.EXPLORE) {
      Game.getActive().CONTROLLERS.getPlayerController().startMoving(direction);
    } else if (mode == GameMode.LOOK) {
      cursorMovingIn = direction;
    }

  }

  @Override
  public void receiveDirectionsCleared() {

    if (mode == GameMode.EXPLORE) {
      Game.getActive().CONTROLLERS.getPlayerController().stopMoving();
    } else if (mode == GameMode.LOOK) {
      cursorMovingIn = null;
    }

  }

  public GameMode getMode() {
    return mode;
  }

  public Point getCursorTarget() {
    return new Point(cursorTarget);
  }

  public List<KeyListener> getKeyListeners() {
    return keyListeners;
  }

}
