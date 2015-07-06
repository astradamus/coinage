package game.input;

import controller.action.Action;
import controller.action.Moving;
import controller.action.TurnThenMove;
import controller.action.Turning;
import controller.player.PlayerAgent;
import game.Direction;
import game.physical.Physical;
import world.Coordinate;
import world.MapCoordinate;
import world.World;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameInputSwitch implements DirectionListener, ListSelectionListener {

  private final World world;
  private PlayerAgent playerController;

  private final List<KeyListener> keyListeners = new ArrayList<>();


  private final KeyModifierInterpreter keyModifierInterpreter = new KeyModifierInterpreter();




  private Selector<Coordinate> coordinateSelector = null;
  private Selector<Physical> physicalSelector = null;

  private GameMode gameMode = GameMode.EXPLORE;

  private TargetCursor targetCursor = null;




  public GameInputSwitch(World world) {

    this.world = world;

    keyListeners.add(keyModifierInterpreter);
    keyListeners.add(new NumpadDirectionInterpreter(this));
    keyListeners.add(new ModeCommandInterpreter(world));
    keyListeners.add(new ListSelectInterpreter(this));

  }


  public boolean getActiveWorldMapAreaIsRevealed(Coordinate coordinate) {
    MapCoordinate mapCoordinate = world.convertToMapCoordinate(coordinate);
    return (getPlayerController().getWorldMapRevealedComponent().getAreaIsRevealed(mapCoordinate));
  }

  public void onUpdate() {

    if (targetCursor != null) {
      targetCursor.onUpdate();
    }

    if (startRepeatingMoveDelay > 0) {
      startRepeatingMoveDelay--;
    }
    else if (startRepeatingMoveDelay == 0 && shouldDelayMoveRepeat()) {
      playerController.attemptAction(new Moving(playerController.getActor(), delayedMoveDirection, false));
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
    targetCursor = TargetCursor.makeSquareTargeter(world, coordinateSelector.getSelectOrigin(),
        coordinateSelector.getSelectRange());

  }

  public void beginSelectingPhysical(Selector<Physical> physicalSelector) {
    clearSelectsAndCursor();

    this.physicalSelector = physicalSelector;
    targetCursor = TargetCursor.makeSquareAndListTargeter(world, physicalSelector.getSelectOrigin(),
        physicalSelector.getSelectRange());

  }


  void enterMode(GameMode mode) {
    clearSelectsAndCursor();
    mode.onEnter(world);

    this.gameMode = mode;
  }


  private Direction delayedMoveDirection = null;
  private int startRepeatingMoveDelay = -1;

  @Override
  public void receiveDirection(Direction direction) {

    if (targetCursor != null) {
      targetCursor.setCursorMovingIn(direction);

    } else {

      final KeyModifier keyModifier = keyModifierInterpreter.getLatestModifier();

      final boolean directionIsAlreadyFaced = direction == playerController.getActor().getFacing();

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

  @Override
  public void receiveDirectionsCleared() {

    if (targetCursor == null) {
      playerController.getActor().doNotRepeatAction();
      terminateRepeatingMoveTimer();
    } else {
      targetCursor.setCursorMovingIn(null);
    }

  }


  private void turnThenMove(Direction direction) {
    final TurnThenMove action = new TurnThenMove(playerController.getActor(), direction, false);

    if (shouldDelayMoveRepeat()) {
      delayRepeatOfMove(action, direction);
    }

    playerController.attemptAction(action);
  }

  private void turn(Direction direction) {
    playerController.attemptAction(new Turning(playerController.getActor(), direction));
  }

  private void move(Direction direction) {
    final Moving moving = new Moving(playerController.getActor(), direction, false);

    if (shouldDelayMoveRepeat()) {
      delayRepeatOfMove(moving, direction);
    }

    playerController.attemptAction(moving);
  }


  private boolean shouldDelayMoveRepeat() {
    return playerController.getActor().isFreeToAct(); // Prevents re-delay when changing directions.
  }

  private void delayRepeatOfMove(Action action, Direction direction) {
    action.doNotRepeat();
    delayedMoveDirection = direction;
    startRepeatingMoveDelay = 15;
  }


  private void terminateRepeatingMoveTimer() {
    delayedMoveDirection = null;
    startRepeatingMoveDelay = -1;
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
      physicalSelector.execute(world.getSquare(targetCursor.getTarget()).getAll()
          .get(targetCursor.getListSelectIndex()));
      physicalSelector = null;
    }

  }



  public void setPlayerController(PlayerAgent playerController) {
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


  public PlayerAgent getPlayerController() {
    return playerController;
  }

  public GameMode getGameMode() {
    return gameMode;
  }

  public List<KeyListener> getKeyListeners() {
    return keyListeners;
  }

}
