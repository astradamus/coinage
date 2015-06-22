package game.input;

import actor.Actor;
import controller.action.Action;
import controller.action.Moving;
import controller.action.TurnThenMove;
import controller.action.Turning;
import controller.player.PlayerController;
import game.Direction;
import game.physical.Physical;
import world.Coordinate;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameInputSwitch implements DirectionListener, ListSelectionListener {

  private PlayerController playerController;

  private final List<KeyListener> keyListeners = new ArrayList<>();


  private Selector<Coordinate> coordinateSelector = null;
  private Selector<Physical> physicalSelector = null;

  private GameMode gameMode = GameMode.EXPLORE;

  private TargetCursor targetCursor = null;




  public GameInputSwitch() {

    keyListeners.add(new NumpadDirectionInterpreter(this));
    keyListeners.add(new ModeCommandInterpreter());
    keyListeners.add(new ListSelectInterpreter(this));

  }



  public void onUpdate() {

    if (targetCursor != null) {
      targetCursor.onUpdate();
    }

    if (startRepeatingMoveDelay > 0) {
      startRepeatingMoveDelay--;
    }
    else if (startRepeatingMoveDelay == 0 && playerController.isFreeToAct()) {
      final Actor playerActor = playerController.getActor();
      playerController.attemptAction(new Moving(playerActor,playerActor.getFacing(),false));
      terminateRepeatingMoveTimer();
    }


  }

  public String getCurrentPrompt() {
    if (coordinateSelector != null) return coordinateSelector.getPrompt();
    if (physicalSelector != null) return physicalSelector.getPrompt();
    return gameMode.getPrompt();
  }

  private void clearSelectsAndCursor() {
    coordinateSelector = null;
    physicalSelector = null;
    targetCursor = null;
  }

  public void beginSelectingCoordinate(Selector<Coordinate> coordinateSelector) {
    clearSelectsAndCursor();

    this.coordinateSelector = coordinateSelector;
    targetCursor = TargetCursor.makeSquareTargeter(coordinateSelector.getSelectOrigin(),
        coordinateSelector.getSelectRange());

  }

  public void beginSelectingPhysical(Selector<Physical> physicalSelector) {
    clearSelectsAndCursor();

    this.physicalSelector = physicalSelector;
    targetCursor = TargetCursor.makeSquareAndListTargeter(physicalSelector.getSelectOrigin(),
        physicalSelector.getSelectRange());

  }


  void enterMode(GameMode mode) {
    clearSelectsAndCursor();
    mode.onEnter();

    this.gameMode = mode;
  }



  private int startRepeatingMoveDelay = -1;

  @Override
  public void receiveDirection(Direction direction, KeyModifier modifier) {

    if (targetCursor != null) {
      targetCursor.setCursorMovingIn(direction);

    } else {

      Actor playerActor = playerController.getActor();

      boolean needsToTurn = direction != playerController.getActor().getFacing();
      boolean turningWithoutMoving = modifier == KeyModifier.CTRL;
      boolean walkingWithoutTurning = modifier == KeyModifier.SHIFT;

      if (turningWithoutMoving && needsToTurn) {
        playerController.attemptAction(new Turning(playerActor, direction));

      } else if (!turningWithoutMoving && (!needsToTurn || walkingWithoutTurning)) {
        final Moving moving = new Moving(playerActor, direction, walkingWithoutTurning);

        if (playerController.isFreeToAct()) {
          delayRepeatOfMove(moving);
        }

        playerController.attemptAction(moving);

      } else {
        final TurnThenMove turnThenMove = new TurnThenMove(playerActor, direction, false);

        if (playerController.isFreeToAct()) {
          delayRepeatOfMove(turnThenMove);
        }

        playerController.attemptAction(turnThenMove);

      }

    }

  }

  private void delayRepeatOfMove(Action action) {
    action.doNotRepeat();
    startRepeatingMoveDelay = 25;
  }

  private void terminateRepeatingMoveTimer() {
    startRepeatingMoveDelay = -1;
  }


  @Override
  public void receiveDirectionsCleared() {

    if (targetCursor == null) {
      playerController.doNotRepeatAction();
      terminateRepeatingMoveTimer();
    } else {
      targetCursor.setCursorMovingIn(null);
    }

  }

  @Override
  public void receiveSelectScroll(int deltaY) {

    if (targetCursor != null) {
      targetCursor.scrollSelection(deltaY);
    }

  }


  @Override
  public void receiveSubmitSelection() {

    if (coordinateSelector != null) {
      coordinateSelector.execute(targetCursor.getTarget());
      coordinateSelector = null;
    }
    else if (physicalSelector != null) {
      physicalSelector.execute(targetCursor.getTarget().getSquare().getAll()
          .get(targetCursor.getListSelectIndex()));
      physicalSelector = null;
    }

  }



  public void setPlayerController(PlayerController playerController) {
    this.playerController = playerController;
  }

  public void setTargetCursor(TargetCursor targetCursor) {
    this.targetCursor = targetCursor;
  }



  public Coordinate getPlayerTarget() {
    if (targetCursor != null) {
      return targetCursor.getTarget();
    }
    return null;
  }

  public Integer getPlayerSelection() {
    if (targetCursor != null) {
      return targetCursor.getListSelectIndex();
    }
    return null;
  }


  public PlayerController getPlayerController() {
    return playerController;
  }

  public GameMode getGameMode() {
    return gameMode;
  }

  public List<KeyListener> getKeyListeners() {
    return keyListeners;
  }

}
