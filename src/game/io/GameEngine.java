package game.io;

import game.Game;
import game.TimeMode;
import game.io.display.GameDisplay;
import game.io.input.GameInput;

import java.security.InvalidParameterException;
import java.util.Stack;

/**
 *
 */
public class GameEngine {

  private static final int MILLISECONDS_PER_HEARTBEAT = 20;


  private static Stack<TimeMode> TIME_MODE = new Stack<>();
  static {TIME_MODE.push(TimeMode.LIVE);}



  private static Game runningGame;

  private static Thread thread;


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



  private static Runnable gameLoop = new Runnable() {
    @Override
    public void run() {
      synchronized (this) {
        while (!thread.isInterrupted()) {

          // Freeze the game (stop sending state updates) if the Engine has been paused, but
          //   continue sending Display updates.

          GameInput.onUpdate();

          TimeMode timeMode = TIME_MODE.peek();
          if (timeMode == TimeMode.LIVE
              || (timeMode == TimeMode.PRECISION && !runningGame.getActivePlayerActor().isFreeToAct())) {
            runningGame.update();
          }

          GameDisplay.onUpdate();


          try {
            wait(MILLISECONDS_PER_HEARTBEAT);
          } catch (InterruptedException e) {
            e.printStackTrace();
            break;
          }
        }
      }
    }
  };


  static void start() {
    if (thread == null || !thread.isAlive()) {
      thread = new Thread(gameLoop);
      thread.start();
    } else {
      System.out.println("Tried to start thread when one was already running.");
    }
  }

  static void stop() {
    if (thread != null && thread.isAlive()) {
      thread.interrupt();
    } else {
      System.out.println("Tried to stop thread when none were running.");
    }
  }

  public static void loadRunningGame(Game runningGame) {
    if (GameEngine.runningGame != null) {
      throw new IllegalStateException("Already running a game, must first call unloadRunningGame().");
    }
    GameEngine.runningGame = runningGame;
  }

  /**
   * @param runningGame Must be supplied to ensure this method is only called from high places.
   * @throws InvalidParameterException If the supplied game is null or does not match the currently
   *                    running game.
   */
  public static void unloadRunningGame(Game runningGame) {
    if (GameEngine.runningGame != runningGame) {
      throw new InvalidParameterException("Game parameter does not match currently running game.");
    }
    GameEngine.runningGame = null;
  }

}
