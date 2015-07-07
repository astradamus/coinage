package game.io;

import actor.ActorTemplate;
import game.Game;
import game.GameBuilder;
import game.io.display.GameDisplay;
import game.io.input.GameInput;
import thing.ThingTemplate;
import utils.Dimension;

/**
 *
 */
public class GameLoader {

  static {

    ThingTemplate.loadThings();
    ActorTemplate.loadActors();

    GameInput.initialize();

    GameDisplay.addKeyListeners(GameInput.getKeyListeners());

  }

  private static Game runningGame;

  static void load(Game game) {
    if (runningGame != null) {
      throw new IllegalStateException("Already running a game, must first call unload().");
    }
    runningGame = game;
    GameInput.loadRunningGame(game);
    GameDisplay.loadRunningGame(game);
    GameDisplay.recalculateSize();
    GameEngine.loadRunningGame(game);
    GameEngine.start();
  }

  static void unload() {
    if (runningGame == null) {
      throw new IllegalStateException("No active Game to unload!");
    }
    GameEngine.stop();
    GameEngine.unloadRunningGame(runningGame);
    GameDisplay.unloadRunningGame(runningGame);
    GameInput.unloadRunningGame(runningGame);
    runningGame = null;
  }


  public static void main(String[] args) {
    load(GameBuilder.newGame(new Dimension(48, 48), new Dimension(24, 24)));
  }

}