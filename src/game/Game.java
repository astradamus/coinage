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

  private PlayerAgent playerAgent;


  public Game(World world) {
    this.world = world;
    this.gameControllers = new GameControllers(this);
  }

  public void setPlayerAgent(PlayerAgent playerAgent) {
    this.playerAgent = playerAgent;
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

  public Actor getActivePlayerActor() {
    return playerAgent.getActor();
  }

  public boolean getWorldMapAreaIsRevealed(Coordinate coordinate) {
    MapCoordinate mapCoordinate = world.convertToMapCoordinate(coordinate);
    return (playerAgent.getWorldMapRevealedComponent().getAreaIsRevealed(mapCoordinate));
  }


  private final Reporter reporter = new Reporter();

  public Reporter getReporter() {
    return reporter;
  }

  public final class Reporter {

    public boolean getActorIsPlayer(Actor actor) {
      return  (getActivePlayerActor() == actor);
    }

    public World getWorld() {
      return world; // todo Set up a world reporter that offers only immutable view of world.
    }

  }

}