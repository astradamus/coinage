package controller;

import actor.Actor;
import world.Area;

import java.util.Set;

public interface ControllerInterface {

  void onLocalityChanged(Controller controller, Area from, Area to);

  void reevaluateActiveAreas();

  Set<Actor> requestActorsInMyArea(ActorAgent actorAgent);

}
