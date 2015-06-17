package game;

import controller.player.PlayerController;
import game.display.GameDisplay;
import game.input.GameInputSwitch;
import thing.ThingFactory;
import utils.Dimension;
import world.World;

import java.awt.event.KeyListener;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 *
 */
public class Game {

  public static final int VISUAL_PRIORITY__TERRAIN = 0;
  public static final int VISUAL_PRIORITY__THINGS = 10;
  public static final int VISUAL_PRIORITY__ACTORS = 100;

  public static final Random RANDOM = new Random();

  static Game ACTIVE;

  private static Stack<TimeMode> TIME_MODE = new Stack<>();
  static {TIME_MODE.push(TimeMode.LIVE);}


  public static Game getActive() {
    return ACTIVE;
  }

  public static TimeMode getTimeMode() {
    return TIME_MODE.peek();
  }

  public static void setTimeMode(TimeMode timeMode) {
    if (TIME_MODE.contains(timeMode)) {

      while (TIME_MODE.peek() != timeMode) {
        TIME_MODE.pop();
      }

    } else {
      TIME_MODE.push(timeMode);
    }
  }

  public static void revertTimeMode() {
    TIME_MODE.pop();
  }



  public static PlayerController getActivePlayer() {
    return ACTIVE.INPUT_SWITCH.getPlayerController();
  }

  public static World getActiveWorld() {
    return ACTIVE.WORLD;
  }

  public static GameControllers getActiveControllers() {
    return ACTIVE.CONTROLLERS;
  }

  public static GameInputSwitch getActiveInputSwitch() {
    return ACTIVE.INPUT_SWITCH;
  }





  final GameInputSwitch INPUT_SWITCH;
  final World WORLD;
  final GameControllers CONTROLLERS;

  public Game(World world, GameControllers gameControllers, GameInputSwitch gameInputSwitch) {
    this.WORLD = world;
    this.CONTROLLERS = gameControllers;
    this.INPUT_SWITCH = gameInputSwitch;
  }


  void update() {

    TimeMode timeMode = TIME_MODE.peek();
    if (timeMode == TimeMode.LIVE
        || (timeMode == TimeMode.PRECISION && !getActivePlayer().isFreeToAct())) {
      CONTROLLERS.onUpdate();
    }

    INPUT_SWITCH.onUpdate();
    GameDisplay.onUpdate();

  }



  public static void main(String[] args) {

    GameLoader.newGame(new Dimension(48, 48), new Dimension(24, 24));
    GameDisplay.recalculateSize();

    List<KeyListener> keyListeners = ACTIVE.INPUT_SWITCH.getKeyListeners();
    for (KeyListener keyListener : keyListeners) {
      GameDisplay.addKeyListener(keyListener);
    }

    Game.getActivePlayer().getActor().getInventory().addItem(ThingFactory.makeThing("STONE"));
    Game.getActivePlayer().getActor().getInventory().addItem(ThingFactory.makeThing("STONE"));
    Game.getActivePlayer().getActor().getInventory().addItem(ThingFactory.makeThing("STONE"));
    GameEngine.start();

  }

}