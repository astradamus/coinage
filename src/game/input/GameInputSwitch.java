package game.input;

import game.Direction;
import game.Game;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameInputSwitch implements DirectionListener, ModeListener {

  private final List<KeyListener> keyListeners;

  private final Point cursorTarget = new Point(-1,-1);
  private Direction cursorMovingIn = null;

  private InputMode inputMode = InputMode.EXPLORE;


  public GameInputSwitch() {

    keyListeners = new ArrayList<>();
    keyListeners.add(new KeyboardDirectionInterpreter(this));
    keyListeners.add(new KeyboardInputModeInterpreter(this));

  }



  @Override
  public void receiveMode(InputMode inputMode) {
    if (this.inputMode != inputMode) {

      if (inputMode == InputMode.EXPLORE) {

        cursorTarget.setLocation(-1,-1);
        cursorMovingIn = null;
        this.inputMode = inputMode;
        System.out.println(inputMode.name());
        Game.unpauseGame();

      } else
      if (inputMode == InputMode.LOOK) {

        Point globalCoordinate =
            Game.getActive().CONTROLLERS.getPlayerController().getGlobalCoordinate();
        Point localCoordinate =
            Game.getActive().WORLD.getAreaCoordinateFromGlobalCoordinate(globalCoordinate);

        cursorTarget.setLocation(localCoordinate);
        this.inputMode = inputMode;
        System.out.println(inputMode.name());
        Game.pauseGame();

      }

    }
  }




  private int moveDelay = 0;
  public void onUpdate() {

    if (inputMode == InputMode.LOOK && cursorMovingIn != null) {

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

    if (inputMode == InputMode.EXPLORE) {
      Game.getActive().CONTROLLERS.getPlayerController().startMoving(direction);
    } else if (inputMode == InputMode.LOOK) {
      cursorMovingIn = direction;
    }

  }

  @Override
  public void receiveDirectionsCleared() {

    if (inputMode == InputMode.EXPLORE) {
      Game.getActive().CONTROLLERS.getPlayerController().stopMoving();
    } else if (inputMode == InputMode.LOOK) {
      cursorMovingIn = null;
    }

  }

  public InputMode getInputMode() {
    return inputMode;
  }

  public Point getCursorTarget() {
    return new Point(cursorTarget);
  }

  public List<KeyListener> getKeyListeners() {
    return keyListeners;
  }

}
