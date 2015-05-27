package game;

import game.display.GameDisplay;

/**
 *
 */
public class GameEngine {

  private static boolean gameUpdatesPaused;

  private static Thread thread;
  private static Runnable gameLoop = new Runnable() {
    @Override
    public void run() {
      synchronized (this) {
        while (!thread.isInterrupted()) {

          // Freeze the game (stop sending state updates) if the Engine has been paused, but
          //   continue sending Display updates.
          if (!gameUpdatesPaused) {
            Game.getActive().update();
          }

          GameDisplay.onUpdate();
          try {
            wait(Timing.MILLISECONDS_PER_HEARTBEAT);
          } catch (InterruptedException e) {
            e.printStackTrace();
            break;
          }
        }
      }
    }
  };


  /**
   * Stops the Engine from sending updates to Game. Does NOT stop the Engine from sending updates
   * to GameDisplay.
   */
  static void pauseGame() {
    if (thread == null || !thread.isAlive()) {
      throw new IllegalStateException("Tried to pause when engine was not running.");
    }
    if (!gameUpdatesPaused) {
      gameUpdatesPaused = true;
    } else {
      System.out.println("Tried to pause game when it was already paused.");
    }
  }

  /**
   * Resumes sending of updates to Game.
   */
  static void unpauseGame() {
    if (thread == null || !thread.isAlive()) {
      throw new IllegalStateException("Tried to unpause when engine was not running.");
    }
    if (gameUpdatesPaused) {
      gameUpdatesPaused = false;
    } else {
      System.out.println("Tried to unpause game when it was already unpaused.");
    }
  }

  /**
   * Convenience method to toggle pause state. Should only be used for player controls. Code
   * calling to pause/unpause the game should be aware of the pause state its in already.
   */
  static void togglePauseGame() {
    if (thread == null || !thread.isAlive()) {
      throw new IllegalStateException("Tried to toggle pause when engine was not running.");
    }
    gameUpdatesPaused = !gameUpdatesPaused;
  }


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

}
