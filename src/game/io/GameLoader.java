package game.io;

import game.Game;
import game.GameBuilder;
import game.io.better_ui.GameFrame;
import utils.Dimension;

/**
 *
 */
class GameLoader {

  private static Game runningGame;
  private static GameFrame runningGameFrame;


  private static void load(Game game) {
    if (runningGame != null) {
      throw new IllegalStateException("Already running a game, must first call unload().");
    }
    runningGame = game;
    runningGameFrame = new GameFrame(game, 20);
    GameEngine.loadRunningGame(game);
    GameEngine.start();
  }


  static void unload() {
    if (runningGame == null) {
      throw new IllegalStateException("No active Game to unload!");
    }
    GameEngine.stop();
    GameEngine.unloadRunningGame();
    runningGame = null;
    runningGameFrame = null;
  }


  public static void main(String[] args) {
    load(GameBuilder.newGame(new Dimension(48, 48), new Dimension(24, 24)));
  }
}