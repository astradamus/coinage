package controller.player;

import actor.Actor;
import controller.ActorAgent;
import controller.action.Action;
import controller.action.ActionFlag;
import game.io.display.Event;
import game.io.display.EventLog;
import world.Area;
import world.MapCoordinate;
import world.World;

/**
 * ActorAgent that allows a player control over an actor in the world.
 */
public class PlayerAgent extends ActorAgent {

    private final World world;
    private final WorldMapTracker worldMapTracker;


    public PlayerAgent(Actor actor, World world) {
        super(actor);
        this.worldMapTracker = new WorldMapTracker(world.getWorldSizeInAreas());
        this.world = world;
    }


    @Override
    public void attemptAction(Action action) {
        getActor().startAction(action.playerIsActor());
    }


    @Override
    protected void onActorObserverDisconnected() {
        EventLog.registerEvent(Event.ALERT_MAJOR, "You are dead. Game over.");
    }


    @Override
    public void onActionExecuted(Action action) {

        // Update WorldMapRevealed component accordingly.
        if (action.hasFlag(ActionFlag.ACTOR_CHANGED_AREA)) {

            MapCoordinate playerAt = world.convertToMapCoordinate(getActor().getCoordinate());
            worldMapTracker.setAreaIsRevealed(playerAt);
            getControllerInterface().reevaluateActiveAreas();
        }
    }


    public final WorldMapTracker getWorldMapRevealedComponent() {
        return worldMapTracker;
    }


    @Override
    public Area getLocality(World world) {
        return null;  // PlayerAgents are non-local.
    }
}