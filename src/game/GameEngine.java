package game;

/**
 *
 */
public class GameEngine {

  public static final int MILLISECONDS_PER_HEARTBEAT = 25;

  private static Thread thread;
  private static Runnable gameLoop = new Runnable() {
    @Override
    public void run() {
      synchronized (this) {
        while (!thread.isInterrupted()) {

          // Freeze the game (stop sending state updates) if the Engine has been paused, but
          //   continue sending Display updates.
          Game.getActive().update();
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

}
