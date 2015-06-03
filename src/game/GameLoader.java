package game;

import actor.Actor;
import actor.ActorFactory;
import controller.AnimalController;
import controller.player.PlayerController;
import game.input.GameInputSwitch;
import utils.Dimension;
import world.Coordinate;
import world.World;
import world.WorldFactory;

import java.awt.Point;

/**
 *
 */
public class GameLoader {

  static void unload() {
    if (Game.ACTIVE == null) {
      throw new IllegalStateException("No active Game to unload!");
    }
    Game.ACTIVE = null;
  }

  static void newGame(Dimension areaSizeInSquares, Dimension worldSizeInAreas) {
    if (Game.ACTIVE != null) {
      throw new IllegalStateException("Cannot load Game while another is active, must call " +
          "GameLoader.unload()");
    }

    // produce a map
    World world = WorldFactory.standardGeneration(areaSizeInSquares, worldSizeInAreas);

    // populate with animals and a Human for the player to control
    GameControllers gameControllers = new GameControllers();

    for (int i = 0; i < 500; i++) {
      String id = (i%2 == 0) ? "DOG" : "CAT";
      Actor actor = ActorFactory.makeActor(id);

      Coordinate randomCoordinate = world.makeRandomCoordinate();
      world.put(actor, randomCoordinate);
      gameControllers.register(new AnimalController(actor, randomCoordinate));
    }

    Actor player = ActorFactory.makeActor("HUMAN");
    Coordinate randomCoordinate = world.makeRandomCoordinate();

    world.put(player, randomCoordinate);

    // assign the Human to a PlayerController and register it
    PlayerController playerController = new PlayerController(player,worldSizeInAreas,randomCoordinate);
    gameControllers.register(playerController);

    // set up the GameInputSwitch
    GameInputSwitch gameInputSwitch = new GameInputSwitch();
    gameInputSwitch.setPlayerController(playerController);

    // produce Game instance and assign it to ACTIVE
    Game.ACTIVE = new Game(world, gameControllers, gameInputSwitch);

  }

}
