package game;

import controller.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameControllers {

  private final List<Controller> ACTIVE_CONTROLLERS = new ArrayList<>();
  private final List<Controller> NEW_CONTROLLERS    = new ArrayList<>();


  /**
   * Called every frame by Game.update(). Walks the list of GameControllers (in getRolledInitiative()
   * order). For each, it calls onUpdate() and then sorts the Controller anew into a second list.
   * After the initial list is walked, the second list becomes the initial list for the next frame.
   */
  public void onUpdate() {
    while(!ACTIVE_CONTROLLERS.isEmpty()) {
      Controller controller = ACTIVE_CONTROLLERS.get(0);
      controller.onUpdate();
      reregister(controller);
    }

    ACTIVE_CONTROLLERS.addAll(NEW_CONTROLLERS);
    NEW_CONTROLLERS.clear();
  }


  public void register(Controller controller) {
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

  void reregister(Controller controller) {
    if (ACTIVE_CONTROLLERS.remove(controller)) {
      register(controller);
    } else {
      System.out.println("Tried to reregister an unregistered controller.");
    }
  }

}
