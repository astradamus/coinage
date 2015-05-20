package game;

import controller.Controller;
import world.World;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class Game {

  public static final Random RANDOM = new Random();

  public static final int VISUAL_PRIORITY__TERRAIN = 0;
  public static final int VISUAL_PRIORITY__THINGS = 10;
  public static final int VISUAL_PRIORITY__ACTORS = 100;



  public static World WORLD;
  private static final List<KeyListener> INPUT_HANDLERS = new ArrayList<>();
  private static final List<Controller> ACTIVE_CONTROLLERS = new ArrayList<>();
  private static final List<Controller> NEW_CONTROLLERS = new ArrayList<>();

  public static void register(KeyListener keyListener) {
    if (INPUT_HANDLERS.contains(keyListener)) {
      System.out.println("Tried to register an already registered KeyListener.");
    } else {
      INPUT_HANDLERS.add(keyListener);
      if (Engine.display != null) {
        Engine.display.addKeyListener(keyListener);
      }
    }
  }

  public static void unregister(KeyListener keyListener) {
    if (INPUT_HANDLERS.remove(keyListener)) {
      if (Engine.display != null) {
        Engine.display.removeKeyListener(keyListener);
      }
    } else {
      System.out.println("Tried to unregister a KeyListener that was not registered.");
    }
  }

  public static void register(Controller controller) {
    if (ACTIVE_CONTROLLERS.contains(controller) || NEW_CONTROLLERS.contains(controller)) {
      System.out.println("Tried to register an already registered Controller.");
    } else {
      for (int i = 0; i < NEW_CONTROLLERS.size(); i++) {
        if (controller.getRolledInitiative() > NEW_CONTROLLERS.get(i).getRolledInitiative()) {
          NEW_CONTROLLERS.add(i,controller);
          return;
        }
      }
      NEW_CONTROLLERS.add(controller);
    }
  }

  private static void reregister(Controller controller) {
    if (ACTIVE_CONTROLLERS.remove(controller)) {
      register(controller);
    } else {
      System.out.println("Tried to reregister an unregistered controller.");
    }
  }


  /**
   * Called every frame by Game.Engine. Walks the list of Controllers (in getRolledInitiative()
   * order). For each, it calls onUpdate() and then sorts the Controller anew into a second list.
   * After the initial list is walked, the second list becomes the initial list for the next frame.
   */
  private static void update() {

    while(!ACTIVE_CONTROLLERS.isEmpty()) {
      Controller cont = ACTIVE_CONTROLLERS.get(0);
      cont.onUpdate(WORLD);
      reregister(cont);
    }

    ACTIVE_CONTROLLERS.addAll(NEW_CONTROLLERS);
    NEW_CONTROLLERS.clear();

  }

  /**
   * Contains game loop.
   */
  public static class Engine {
    private static Runnable runnable = new Runnable() {
      @Override
      public void run() {
        synchronized (this) {
          while (!thread.isInterrupted()) {
            update();
            display.repaint();
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
    private static Thread thread;
    private static JFrame display;
    public static void start(JFrame display) {
      if (thread == null || !thread.isAlive()) {
        Engine.display = display;
        for (KeyListener keyListener : INPUT_HANDLERS) {
          display.addKeyListener(keyListener);
        }
        thread = new Thread(runnable);
        thread.start();
      } else {
        System.out.println("Tried to start thread when it was already running.");
      }
    }
    public static void stop() {
      if (thread != null && thread.isAlive()) {
        thread.interrupt();
        display = null;
      } else {
        System.out.println("Tried to stop thread when it was not running.");
      }
    }
  }

}