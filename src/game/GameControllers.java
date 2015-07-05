package game;

import actor.Actor;
import controller.ActorAgent;
import controller.Controller;
import controller.action.Action;
import game.physical.PhysicalFlag;
import world.Area;
import world.Coordinate;
import world.World;

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
public class GameControllers implements Executor {

  public static final int CONTROLLER_PROCESS_RADIUS = 10;


  private final World world;

  private final Map<Area,Set<Controller>> controllerLocations = new HashMap<>();
  private Set<Area> activeAreas = null;

  public GameControllers(World world) {
    this.world = world;
    world.getAllAreas().forEach(area -> controllerLocations.put(area, new HashSet<>()));
    controllerLocations.put(null, new HashSet<>()); // null contains non-local controllers
  }

  public void onPlayerChangedArea() {
    playerChangedArea = true;
  }

  public void moveController(ActorAgent actorAgent, Area from, Area to) {
    if (controllerLocations.get(from).remove(actorAgent)) {
      controllerLocations.get(to).add(actorAgent);
    }
  }

  public void addController(Controller controller) {
    NEXT_CONTROLLERS.add(controller);
    controllerLocations.get(world.getArea(controller.getLocality())).add(controller);
  }

  public void removeController(Controller controller) {
    DEAD_CONTROLLERS.add(controller);
    controllerLocations.get(world.getArea(controller.getLocality())).remove(controller);
  }



  // Starts true so we calculate active areas on first update.
  private boolean playerChangedArea = true;

  private final List<Controller> ACTIVE_CONTROLLERS = new ArrayList<>();
  private final Set<Controller>  NEXT_CONTROLLERS   = new HashSet<>();
  private final Set<Controller>  DEAD_CONTROLLERS   = new HashSet<>();



  @Override
  public boolean executeAction(Action action) {
    return action.perform(world);
  }

  /**
   * Called every frame by Game.update(). Walks the list of GameControllers (in getRolledInitiative()
   * order). For each, it calls onUpdate() and then sorts the Controller anew into a second list.
   * After the initial list is walked, the second list becomes the initial list for the next frame.
   */
  public void onUpdate() {


    // Get all active controllers that are still in processing range. By doing this we avoid
    // the complex task of removing controllers from ACTIVE on the fly, as they or the range move.
    List<Controller> activeAndInRange = ACTIVE_CONTROLLERS.stream()
        .filter(active -> activeAreas.contains(world.getArea(active.getLocality())))
        .collect(Collectors.toList());


    // First, update each controller, skipping any that are dead.
    activeAndInRange.stream()
        .filter(controller -> !DEAD_CONTROLLERS.contains(controller))
        .forEach(controller -> controller.onUpdate(this));

    if (!playerChangedArea) {

      // If player didn't change area, register same actives for next turn.
      activeAndInRange.forEach(NEXT_CONTROLLERS::add);

    } else {

      // If player changed area, update active areas and register all controllers therein.
      calculateActiveAreasAndControllers();
      playerChangedArea = false;

    }

    // Prune any controllers marked dead with removeController().
    DEAD_CONTROLLERS.forEach(NEXT_CONTROLLERS::remove);

    performNextTurnSort();

  }


  private void calculateActiveAreasAndControllers() {
    Coordinate playerAt = Game.getActivePlayerActor().getCoordinate();
    activeAreas
        = world.getAllAreasWithinRange(playerAt, CONTROLLER_PROCESS_RADIUS);
    activeAreas.add(null); // Null contains non-local controllers. Always process it!

    for (Area area : activeAreas) {
      NEXT_CONTROLLERS.addAll(controllerLocations.get(area));
    }
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

  public Set<Actor> getActorsInArea(Area area) {

    HashSet<Actor> set = new HashSet<>();

    set.addAll(controllerLocations.get(area).stream()
        .filter(ActorAgent.class::isInstance)
        .map(agent -> ((ActorAgent) agent).getActor())
        .collect(Collectors.toSet()));

    // If the player is here and alive, include them in the return.
    final Actor playerActor = Game.getActivePlayerActor();
    if (!playerActor.hasFlag(PhysicalFlag.DEAD)
        && world.getArea(playerActor.getCoordinate()) == area) {
      set.add(playerActor);
    }

    return set;

  }

}