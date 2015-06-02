package game;

import controller.player.Component_WorldMapRevealed;
import game.display.GameDisplay;
import game.input.GameInputSwitch;
import game.input.InputMode;
import utils.Dimension;
import world.Area;
import world.World;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class Game {

  public static final int VISUAL_PRIORITY__TERRAIN = 0;
  public static final int VISUAL_PRIORITY__THINGS = 10;
  public static final int VISUAL_PRIORITY__ACTORS = 100;

  public static final Random RANDOM = new Random();

  static Game ACTIVE;
  private static boolean gameIsPaused;


  public static Game getActive() {
    return ACTIVE;
  }



  public static InputMode getActiveInputMode() {
    return ACTIVE.INPUT_SWITCH.getInputMode();
  }

  public static Component_WorldMapRevealed getActivePlayerWorldMapRevealedComponent() {
    return ACTIVE.INPUT_SWITCH.getPlayerController().getWorldMapRevealedComponenet();
  }

  public static Point getActivePlayerGlobalCoordinate() {
    return ACTIVE.INPUT_SWITCH.getPlayerController().getGlobalCoordinate();
  }

  public static Point getActivePlayerWorldCoordinate() {
    return ACTIVE.WORLD.getWorldCoordinateFromGlobalCoordinate(getActivePlayerGlobalCoordinate());
  }

  public static Area getActivePlayerArea() {
    Point activePlayerGlobalCoordinate = getActivePlayerGlobalCoordinate();
    return ACTIVE.WORLD.getAreaByGlobalCoordinate(activePlayerGlobalCoordinate.x,
        activePlayerGlobalCoordinate.y);
  }

  public static Point getActiveCursorTarget() {
    return ACTIVE.INPUT_SWITCH.getCursorTarget();
  }



  /**
   * Stops the Engine from sending updates to Game. Does NOT stop the Engine from sending updates
   * to GameDisplay.
   */
  public static void pauseGame() {
    if (!gameIsPaused) {
      gameIsPaused = true;
    } else {
      System.out.println("Tried to pause game when it was already paused.");
    }
  }

  /**
   * Resumes sending of updates to Game.
   */
  public static void unpauseGame() {
    if (gameIsPaused) {
      gameIsPaused = false;
    } else {
      System.out.println("Tried to unpause game when it was already unpaused.");
    }
  }

  /**
   * Convenience method to toggle pause state. Should only be used for player controls. Code
   * calling to pause/unpause the game should be aware of the pause state its in already.
   */
  public static void togglePauseGame() {
    gameIsPaused = !gameIsPaused;
  }





  final GameInputSwitch INPUT_SWITCH;
  public final World WORLD;
  final GameControllers CONTROLLERS;

  public Game(World world, GameControllers gameControllers, GameInputSwitch gameInputSwitch) {
    this.WORLD = world;
    this.CONTROLLERS = gameControllers;
    this.INPUT_SWITCH = gameInputSwitch;
  }


  void update() {

    // do not update controllers if the game is paused.
    if (!gameIsPaused) {
      CONTROLLERS.onUpdate();
    }

    INPUT_SWITCH.onUpdate();
    GameDisplay.onUpdate();

  }



  public static void main(String[] args) {

    GameLoader.newGame(new Dimension(64, 64), new Dimension(18, 18));
    GameDisplay.recalculateSize();

    List<KeyListener> keyListeners = ACTIVE.INPUT_SWITCH.getKeyListeners();
    for (KeyListener keyListener : keyListeners) {
      GameDisplay.addKeyListener(keyListener);
    }

    GameEngine.start();

  }

}