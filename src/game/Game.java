package game;

import actor.Actor;
import controller.player.PlayerAgent;
import world.Coordinate;
import world.MapCoordinate;
import world.World;

import java.util.Random;

/**
 *
 */
public class Game {

    public static final Random RANDOM = new Random();

    private final World world;
    private final GameControllers gameControllers;

    private final Informer informer = new Informer();

    private PlayerAgent playerAgent;


    public Game(World world) {
        this.world = world;
        this.gameControllers = new GameControllers(this);
    }


    public void update() {
        gameControllers.onUpdate();
    }


    public World getWorld() {
        return world;
    }


    public GameControllers getGameControllers() {
        return gameControllers;
    }


    public PlayerAgent getPlayerAgent() {
        return playerAgent;
    }


    public void setPlayerAgent(PlayerAgent playerAgent) {
        this.playerAgent = playerAgent;
    }


    public Actor getActivePlayerActor() {
        return playerAgent.getActor();
    }


    public boolean getWorldMapAreaIsRevealed(MapCoordinate mapCoordinate) {
        return (playerAgent.getWorldMapRevealedComponent().getAreaIsRevealed(mapCoordinate));
    }


    public Informer getInformer() {
        return informer;
    }


    public final class Informer {

        public boolean getActorIsPlayer(Actor actor) {
            return (getActivePlayerActor() == actor);
        }


        public World.Informer getWorldInformer() {
            return world.getInformer();
        }
    }
}