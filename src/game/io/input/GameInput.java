package game.io.input;

import controller.action.Action;
import controller.action.Moving;
import controller.action.TurnThenMove;
import controller.action.Turning;
import game.Direction;
import game.Game;
import game.physical.Physical;
import world.Coordinate;

import java.awt.event.KeyListener;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameInput {

  private static final List<KeyListener> keyListeners = new ArrayList<>();
  private static final KeyModifierInterpreter keyModifierInterpreter = new KeyModifierInterpreter();

  private static Game runningGame;
  private static GameMode gameMode = GameMode.EXPLORE;

  private static Selector<Coordinate> coordinateSelector = null;
  private static Selector<Physical> physicalSelector = null;
  private static TargetCursor targetCursor = null;

  private static Direction delayedMoveDirection = null;
  private static int startRepeatingMoveDelay = -1;


  public static void initialize() {

    final DirectionListener directionListener = new DirectionListener() {
      @Override
      public void receiveDirection(Direction direction) {
        GameInput.receiveDirection(direction);
      }


      @Override
      public void receiveDirectionsCleared() {
        GameInput.receiveDirectionsCleared();
      }
    };

    final ListSelectionListener listSelectionListener = new ListSelectionListener() {
      @Override
      public void receiveSelectScroll(int deltaY) {
        GameInput.receiveSelectScroll(deltaY);
      }


      @Override
      public void receiveSubmitSelection() {
        GameInput.receiveSubmitSelection();
      }
    };

    keyListeners.add(keyModifierInterpreter);
    keyListeners.add(new NumpadDirectionInterpreter(directionListener));
    keyListeners.add(new ListSelectInterpreter(listSelectionListener));
    keyListeners.add(new ModeCommandInterpreter());
  }


  public static void loadRunningGame(Game runningGame) {
    if (GameInput.runningGame != null) {
      throw new IllegalStateException(
          "Already running a game, must first call unloadRunningGame().");
    }
    GameInput.runningGame = runningGame;
  }


  /**
   * @param runningGame Must be supplied to ensure this method is only called from high places.
   * @throws InvalidParameterException If the supplied game is null or does not match the currently
   *                                   running game.
   */
  public static void unloadRunningGame(Game runningGame) {
    if (GameInput.runningGame != runningGame) {
      throw new InvalidParameterException("Game parameter does not match currently running game.");
    }
    GameInput.runningGame = null;
  }


  static Game getRunningGame() {
    return GameInput.runningGame;
  }


  public static void onUpdate() {

    if (targetCursor != null) {
      targetCursor.onUpdate();
    }

    if (startRepeatingMoveDelay > 0) {
      startRepeatingMoveDelay--;
    }
    else if (startRepeatingMoveDelay == 0 && shouldDelayMoveRepeat()) {
      runningGame.getPlayerAgent().attemptAction(
          new Moving(runningGame.getActivePlayerActor(), delayedMoveDirection, false));
      terminateRepeatingMoveTimer();
    }
  }


  public static String getCurrentPrompt() {
    if (coordinateSelector != null) {
      return coordinateSelector.getPrompt();
    }
    if (physicalSelector != null) {
      return physicalSelector.getPrompt();
    }
    return gameMode.getPrompt();
  }


  private static void clearSelectsAndCursor() {
    coordinateSelector = null;
    physicalSelector = null;
    targetCursor = null;
  }


  static void beginSelectingCoordinate(Selector<Coordinate> coordinateSelector) {
    clearSelectsAndCursor();

    GameInput.coordinateSelector = coordinateSelector;
    targetCursor = TargetCursor
        .makeSquareTargeter(runningGame.getWorld(), coordinateSelector.getSelectOrigin(),
            coordinateSelector.getSelectRange());
  }


  public static void beginSelectingPhysical(Selector<Physical> physicalSelector) {
    clearSelectsAndCursor();

    GameInput.physicalSelector = physicalSelector;
    targetCursor = TargetCursor
        .makeSquareAndListTargeter(runningGame.getWorld(), physicalSelector.getSelectOrigin(),
            physicalSelector.getSelectRange());
  }


  static void enterMode(GameMode mode) {
    clearSelectsAndCursor();
    mode.onEnter();

    GameInput.gameMode = mode;
  }


  private static void receiveDirection(Direction direction) {

    if (targetCursor != null) {
      targetCursor.setCursorMovingIn(direction);
    }
    else {

      final KeyModifier keyModifier = keyModifierInterpreter.getLatestModifier();

      final boolean directionIsAlreadyFaced =
          direction == runningGame.getActivePlayerActor().getFacing();

      final boolean shouldTurn = !directionIsAlreadyFaced && keyModifier != KeyModifier.SHIFT;
      final boolean shouldMove = keyModifier != KeyModifier.CTRL;

      if (shouldTurn && shouldMove) {
        turnThenMove(direction);
      }
      else if (shouldTurn) {
        turn(direction);
      }
      else if (shouldMove) {
        move(direction);
      }
    }
  }


  private static void receiveDirectionsCleared() {

    if (targetCursor == null) {
      runningGame.getActivePlayerActor().doNotRepeatAction();
      terminateRepeatingMoveTimer();
    }
    else {
      targetCursor.setCursorMovingIn(null);
    }
  }


  private static void turnThenMove(Direction direction) {
    final TurnThenMove action =
        new TurnThenMove(runningGame.getActivePlayerActor(), direction, false);

    if (shouldDelayMoveRepeat()) {
      delayRepeatOfMove(action, direction);
    }

    runningGame.getPlayerAgent().attemptAction(action);
  }


  private static void turn(Direction direction) {
    runningGame.getPlayerAgent()
        .attemptAction(new Turning(runningGame.getActivePlayerActor(), direction));
  }


  private static void move(Direction direction) {
    final Moving moving = new Moving(runningGame.getActivePlayerActor(), direction, false);

    if (shouldDelayMoveRepeat()) {
      delayRepeatOfMove(moving, direction);
    }

    runningGame.getPlayerAgent().attemptAction(moving);
  }


  private static boolean shouldDelayMoveRepeat() {
    return runningGame.getActivePlayerActor()
        .isFreeToAct(); // Prevents re-delay when changing directions.
  }


  private static void delayRepeatOfMove(Action action, Direction direction) {
    action.doNotRepeat();
    delayedMoveDirection = direction;
    startRepeatingMoveDelay = 15;
  }


  private static void terminateRepeatingMoveTimer() {
    delayedMoveDirection = null;
    startRepeatingMoveDelay = -1;
  }


  private static void receiveSelectScroll(int deltaY) {

    if (targetCursor != null) {
      targetCursor.scrollSelection(deltaY);
    }
  }


  private static void receiveSubmitSelection() {

    if (coordinateSelector != null) {
      coordinateSelector.execute(targetCursor.getTarget());
      coordinateSelector = null;
    }
    else if (physicalSelector != null) {
      physicalSelector.execute(runningGame.getWorld().getSquare(targetCursor.getTarget()).getAll()
          .get(targetCursor.getListSelectIndex()));
      physicalSelector = null;
    }
  }


  static void setTargetCursor(TargetCursor targetCursor) {
    GameInput.targetCursor = targetCursor;
  }


  public static Coordinate getPlayerTarget() {
    if (targetCursor != null) {
      return targetCursor.getTarget();
    }
    return null;
  }


  public static Integer getPlayerSelection() {
    if (targetCursor != null) {
      return targetCursor.getListSelectIndex();
    }
    return null;
  }


  public static GameMode getGameMode() {
    return gameMode;
  }


  public static List<KeyListener> getKeyListeners() {
    return keyListeners;
  }
}