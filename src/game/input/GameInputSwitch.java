package game.input;

import actor.Actor;
import controller.action.Moving;
import controller.player.PlayerController;
import game.Direction;
import game.Game;
import game.Physical;
import game.TimeMode;
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

  @Override
  public void receiveDirection(Direction direction) {

    if (targetCursor == null) {
      Actor playerActor = playerController.getActor();
      playerController.attemptAction(new Moving(playerActor, direction));
    } else {
      targetCursor.setCursorMovingIn(direction);
    }

  }

  @Override
  public void receiveDirectionsCleared() {

    if (targetCursor == null) {
      playerController.attemptAction(null);
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
