package game;

import controller.ActorController;
import controller.Controller;
import world.Area;
import world.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class GameControllers {

  public static final int CONTROLLER_PROCESS_RADIUS = 10;


  private final Map<Area,Set<Controller>> controllerLocations = new HashMap<>();
  private Set<Area> activeAreas = null;

  public GameControllers(Set<Area> allAreas) {
    allAreas.forEach(area -> controllerLocations.put(area, new HashSet<>()));
    controllerLocations.put(null, new HashSet<>()); // null contains non-local controllers
  }

  public void onPlayerChangedArea() {
    playerChangedArea = true;
  }

  public void moveController(ActorController actorController, Area from, Area to) {
    if (controllerLocations.get(from).remove(actorController)) {
      controllerLocations.get(to).add(actorController);
    }
  }

  public void addController(Controller controller) {
    NEXT_CONTROLLERS.add(controller);
    controllerLocations.get(controller.getLocality()).add(controller);
  }

  public void removeController(Controller controller) {
    DEAD_CONTROLLERS.add(controller);
  }



  // Starts true so we calculate active areas on first update.
  private boolean playerChangedArea = true;

  private final List<Controller> ACTIVE_CONTROLLERS = new ArrayList<>();
  private final Set<Controller>  NEXT_CONTROLLERS   = new HashSet<>();
  private final Set<Controller>  DEAD_CONTROLLERS   = new HashSet<>();

  /**
   * Called every frame by Game.update(). Walks the list of GameControllers (in getRolledInitiative()
   * order). For each, it calls onUpdate() and then sorts the Controller anew into a second list.
   * After the initial list is walked, the second list becomes the initial list for the next frame.
   */
  public void onUpdate() {


    // Get all active controllers that are still in processing range. By doing this we avoid
    // the complex task of removing controllers from ACTIVE on the fly, as they or the range move.
    List<Controller> activeAndInRange = ACTIVE_CONTROLLERS.stream()
        .filter(active -> activeAreas.contains(active.getLocality())).collect(Collectors.toList());


    // First, update each.
    activeAndInRange.forEach(controller.Controller::onUpdate);

    if (!playerChangedArea) {

      // If player didn't change area, register same actives for next turn.
      activeAndInRange.forEach(NEXT_CONTROLLERS::add);

    } else {

      // If player changed area, update active areas and register all controllers therein.
      calculateActiveAreasAndControllers();
      playerChangedArea = false;

    }

    // Prune any controllers marked for removal with removeController().
    DEAD_CONTROLLERS.forEach(this::pruneDeadController);

    performNextTurnSort();

  }


  private void calculateActiveAreasAndControllers() {
    Coordinate playerAt = Game.getActivePlayer().getActor().getCoordinate();
    activeAreas
        = Game.getActiveWorld().getAllAreasWithinRange(playerAt, CONTROLLER_PROCESS_RADIUS);
    activeAreas.add(null); // Null contains non-local controllers. Always process it!

    for (Area area : activeAreas) {
      NEXT_CONTROLLERS.addAll(controllerLocations.get(area));
    }
  }


  private void pruneDeadController(Controller controller) {
    NEXT_CONTROLLERS.remove(controller);
    controllerLocations.get(controller.getLocality()).remove(controller);
  }

  private static final Map<Controller, Integer> INITIATIVE_ROLLS = new HashMap<>();
  private static final Comparator<Controller> CONTROLLER_COMPARATOR = Comparator.comparing(INITIATIVE_ROLLS::get);

  private void performNextTurnSort() {

    // Clear this turn's actives.
    ACTIVE_CONTROLLERS.clear();

    // Add the next controllers to active.
    ACTIVE_CONTROLLERS.addAll(NEXT_CONTROLLERS);
    NEXT_CONTROLLERS.clear();

    // Roll initiative for all controllers and then sort by rolls.
    INITIATIVE_ROLLS.clear();
    ACTIVE_CONTROLLERS.forEach(cont -> INITIATIVE_ROLLS.put(cont, cont.getRolledInitiative()));

    Collections.sort(ACTIVE_CONTROLLERS, CONTROLLER_COMPARATOR);

  }

  public <T extends Controller> Set<T> getControllersByArea(Class<T> controllerClass, Area area) {
    HashSet<T> set = new HashSet<>();

    set.addAll(controllerLocations.get(area).stream()
        .filter(controllerClass::isInstance)
        .map(controllerClass::cast)
        .collect(Collectors.toSet()));

    return set;
  }

}
