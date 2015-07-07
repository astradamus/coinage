package game;

import actor.Actor;
import controller.ActorAgent;
import controller.Controller;
import controller.ControllerInterface;
import controller.action.Action;
import game.physical.PhysicalFlag;
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
public class GameControllers implements Executor, ControllerInterface {

  private static final int CONTROLLER_PROCESS_RADIUS = 10;


  private final Game game;

  private final Map<Area,Set<Controller>> controllerLocations = new HashMap<>();
  private Set<Area> activeAreas = null;

  public GameControllers(Game game) {
    this.game = game;
    game.getWorld().getAllAreas().forEach(area -> controllerLocations.put(area, new HashSet<>()));
    controllerLocations.put(null, new HashSet<>()); // null contains non-local controllers
  }

  @Override
  public void reevaluateActiveAreas() {
    reevaluateActives = true;
  }

  @Override
  public void onLocalityChanged(Controller controller, Area from, Area to) {
    if (controllerLocations.get(from).remove(controller)) {
      controllerLocations.get(to).add(controller);
    }
  }

  public void addController(Controller controller) {
    NEXT_CONTROLLERS.add(controller);
    controllerLocations.get(controller.getLocality(game.getWorld())).add(controller);
    controller.setControllerInterface(this);
  }


  // Starts true so we calculate active areas on first update.
  private boolean reevaluateActives = true;

  private final List<Controller> ACTIVE_CONTROLLERS = new ArrayList<>();
  private final Set<Controller>  NEXT_CONTROLLERS   = new HashSet<>();



  @Override
  public boolean executeAction(Action action) {
    return action.perform(game.getWorld());
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
        .filter(active -> activeAreas.contains(active.getLocality(game.getWorld())))
        .collect(Collectors.toList());


    // Update each controller, skipping any that are dead. Don't bother pruning the dead yet, because controllers can
    // die after we've already passed them in this loop.
    activeAndInRange.stream()
        .filter(controller -> controller.getIsStillRunning())
        .forEach(controller -> controller.onUpdate(this));

    // Prune all dead controllers.
    pruneDeadControllers(activeAndInRange);

    if (!reevaluateActives) {

      // Register the same actives for the next turn
      activeAndInRange.forEach(NEXT_CONTROLLERS::add);

    } else {

      // If player changed area, update active areas and register all controllers therein.
      calculateActiveAreasAndControllers();
      reevaluateActives = false;

    }

    performNextTurnSort();

  }

  private void pruneDeadControllers(List<Controller> listToPrune) {
    for (int i = 0; i < listToPrune.size(); i++) {
      Controller cont = listToPrune.get(i);

      if (!cont.getIsStillRunning()) {
        listToPrune.remove(i);
        i--;

        controllerLocations.get(cont.getLocality(game.getWorld())).remove(cont);
        cont.setControllerInterface(null);
      }

    }
  }


  private void calculateActiveAreasAndControllers() {
    final Coordinate playerAt = game.getActivePlayerActor().getCoordinate();
    activeAreas = game.getWorld().getAllAreasWithinRange(playerAt, CONTROLLER_PROCESS_RADIUS);
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

  @Override
  public Set<Actor> requestActorsInMyArea(ActorAgent actorAgent) {
    final Area area = actorAgent.getLocality(game.getWorld());
    return getActorsInArea(area);
  }

  public Set<Actor> getActorsInArea(Area area) {

    HashSet<Actor> set = new HashSet<>();

    set.addAll(controllerLocations.get(area).stream()
        .filter(ActorAgent.class::isInstance)
        .map(agent -> ((ActorAgent) agent).getActor())
        .collect(Collectors.toSet()));

    // If the player is here and alive, include them in the return.
    final Actor playerActor = game.getActivePlayerActor();
    if (!playerActor.hasFlag(PhysicalFlag.DEAD)
        && game.getWorld().getArea(playerActor.getCoordinate()) == area) {
      set.add(playerActor);
    }

    return set;

  }

}