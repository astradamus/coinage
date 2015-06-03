package game.input;

import controller.player.PlayerController;
import game.Direction;
import game.Game;
import world.Coordinate;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameInputSwitch implements DirectionListener, ModeListener {

  private PlayerController playerController;

  private final List<KeyListener> keyListeners;

  private Coordinate cursorTarget = null;
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

        cursorTarget = null;
        cursorMovingIn = null;
        this.inputMode = inputMode;
        Game.unpauseGame();

      } else
      if (inputMode == InputMode.LOOK) {

        cursorTarget = playerController.getCoordinate();
        this.inputMode = inputMode;
        Game.pauseGame();

      } else
      if (inputMode == InputMode.INVENTORY) {

        this.inputMode = inputMode;
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

        Coordinate newCoordinate = Game.getActiveWorld().offsetCoordinateBySquares(cursorTarget,
            cursorMovingIn.relativeX, cursorMovingIn.relativeY);

        if (newCoordinate != null) {

          cursorTarget = newCoordinate;
          moveDelay = 2;

        }

      }

    }

  }


  @Override
  public void receiveDirection(Direction direction) {

    if (inputMode == InputMode.EXPLORE) {
      playerController.startMoving(direction);
    } else if (inputMode == InputMode.LOOK) {
      cursorMovingIn = direction;
    }

  }

  @Override
  public void receiveDirectionsCleared() {

    if (inputMode == InputMode.EXPLORE) {
      playerController.stopMoving();
    } else if (inputMode == InputMode.LOOK) {
      cursorMovingIn = null;
    }

  }

  public void setPlayerController(PlayerController playerController) {
    this.playerController = playerController;
  }

  public PlayerController getPlayerController() {
    return playerController;
  }

  public InputMode getInputMode() {
    return inputMode;
  }

  public Coordinate getCursorTarget() {
    return cursorTarget;
  }

  public List<KeyListener> getKeyListeners() {
    return keyListeners;
  }

}
