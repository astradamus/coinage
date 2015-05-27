package game;

import game.display.GameDisplay;
import utils.Dimension;
import world.World;

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

  public static Game getActive() {
    return ACTIVE;
  }

  public final World WORLD;
  public final Controllers CONTROLLERS;

  public Game(World world, Controllers controllers) {
    this.WORLD = world;
    this.CONTROLLERS = controllers;
  }

  void update() {
    CONTROLLERS.onUpdate();
  }

  public static void main(String[] args) {
    GameLoader.newGame(new Dimension(64, 64), new Dimension(18, 18));
    GameDisplay.recalculateSize();

    // register player keyListeners.
    List<KeyListener> keyListeners = Game.getActive().CONTROLLERS.getPlayerController().getKeyListeners();
    for (KeyListener keyListener : keyListeners) {
      GameDisplay.addKeyListener(keyListener);
    }

    GameEngine.start();
  }

}