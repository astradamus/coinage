package game.input;

import controller.player.PlayerController;
import game.Direction;
import game.Game;
import game.Physical;
import utils.Utils;
import world.Coordinate;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameInputSwitch implements DirectionListener, ModeListener, SelectListener {

  private PlayerController playerController;

  private final List<KeyListener> keyListeners;

  private Coordinate cursorTarget = null;
  private Direction cursorMovingIn = null;

  private Integer listSelectIndex = null;
  private Integer listSelectLength = null;

  private InputMode inputMode = InputMode.EXPLORE;


  public GameInputSwitch() {

    keyListeners = new ArrayList<>();
    keyListeners.add(new KeyboardDirectionInterpreter(this));
    keyListeners.add(new KeyboardInputModeInterpreter(this));
    keyListeners.add(new KeyboardSelectInterpreter(this));

  }



  @Override
  public void receiveMode(InputMode inputMode) {

    if (this.inputMode != InputMode.EXPLORE) {
      // if we are not exploring, accept ONLY "explore"

      if (inputMode == InputMode.EXPLORE) {

        cursorTarget = null;
        cursorMovingIn = null;
        listSelectIndex = null;
        listSelectLength = null;
        this.inputMode = inputMode;
        Game.unpauseGame();

      }

    } else { // if we ARE exploring, accept the following:

      if (inputMode == InputMode.LOOK) {

        cursorTarget = playerController.getCoordinate();
        this.inputMode = inputMode;
        Game.pauseGame();

      } else
      if (inputMode == InputMode.INVENTORY) {

        this.inputMode = inputMode;
        Game.pauseGame();

      } else
      if (inputMode == InputMode.GRAB) {

        cursorTarget = Game.getActivePlayer().getCoordinate();

        listSelectIndex = 0;
        listSelectLength = cursorTarget.getSquare().getAll().size();

        this.inputMode = inputMode;
        Game.pauseGame();

      } else
      if (inputMode == InputMode.DROP) {

        cursorTarget = Game.getActivePlayer().getCoordinate();

        listSelectIndex = 0;
        listSelectLength = playerController.getActor().getInventory().getItemsHeld().size();

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

    } else
    if ((inputMode == InputMode.GRAB || inputMode == InputMode.DROP) && cursorMovingIn != null) {

      if (moveDelay > 0) {

        moveDelay--;

      } else {

        Coordinate playerAt = Game.getActivePlayer().getCoordinate();

        int relativeX = (cursorTarget.globalX - playerAt.globalX) + cursorMovingIn.relativeX;
        int relativeY = (cursorTarget.globalY - playerAt.globalY) + cursorMovingIn.relativeY;

        relativeX = Utils.clamp(relativeX,-1,+1);
        relativeY = Utils.clamp(relativeY,-1,+1);

        listSelectIndex = 0;
        cursorTarget = Game.getActiveWorld().offsetCoordinateBySquares(playerAt,relativeX,
            relativeY);

        moveDelay = 2;

      }
    }

  }


  @Override
  public void receiveDirection(Direction direction) {

    if (inputMode == InputMode.EXPLORE) {
      playerController.startMoving(direction);
    } else if (inputMode == InputMode.LOOK
        || inputMode == InputMode.GRAB
        || inputMode == InputMode.DROP) {
      cursorMovingIn = direction;
    }

  }

  @Override
  public void receiveDirectionsCleared() {

    if (inputMode == InputMode.EXPLORE) {
      playerController.stopMoving();
    } else if (inputMode == InputMode.LOOK
        || inputMode == InputMode.GRAB
        || inputMode == InputMode.DROP) {
      cursorMovingIn = null;
    }

  }

  @Override
  public void receiveSelectScroll(int deltaY) {

    if (inputMode == InputMode.GRAB
        || inputMode == InputMode.DROP) {

      listSelectIndex = Utils.modulus(listSelectIndex + deltaY,listSelectLength);

    }

  }

  @Override
  public void receiveSubmitSelection() {

    if (inputMode == InputMode.GRAB) {

      Physical target = cursorTarget.getSquare().getAll().get(listSelectIndex);
      playerController.startGrabbing(target,cursorTarget);
      receiveMode(InputMode.EXPLORE);

    } else if (inputMode == InputMode.DROP) {

      Physical dropping = playerController.getActor().getInventory().getItemsHeld()
          .get(listSelectIndex);

      playerController.startDropping(dropping,cursorTarget);
      receiveMode(InputMode.EXPLORE);

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

  public Integer getListSelectIndex() {
    return listSelectIndex;
  }

  public List<KeyListener> getKeyListeners() {
    return keyListeners;
  }

}
