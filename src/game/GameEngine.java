package game;

/**
 *
 */
public class GameEngine {

  private static Thread thread;
  private static Runnable gameLoop = new Runnable() {
    @Override
    public void run() {
      synchronized (this) {
        while (!thread.isInterrupted()) {
          Game.getActive().update();
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
