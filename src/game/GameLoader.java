package game;

import actor.Actor;
import actor.ActorFactory;
import controller.AnimalController;
import controller.PlayerController;
import utils.Dimension;
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
    Controllers controllers = new Controllers();

    for (int i = 0; i < 500; i++) {
      String id = (i%2 == 0) ? "DOG" : "CAT";
      Actor actor = ActorFactory.makeActor(id);

      Point point = new Point(Game.RANDOM.nextInt(world.getGlobalSizeInSquares().getWidth()),
          Game.RANDOM.nextInt(world.getGlobalSizeInSquares().getHeight()));
      world.put(actor, point.x, point.y);
      controllers.register(new AnimalController(actor, point));
    }

    Actor player = ActorFactory.makeActor("HUMAN");
    int playerX = Game.RANDOM.nextInt(world.getGlobalSizeInSquares().getWidth());
    int playerY = Game.RANDOM.nextInt(world.getGlobalSizeInSquares().getHeight());
    Point playerLocation = new Point(playerX,playerY);

    world.put(player, playerX, playerY);

    // assign the Human to a PlayerController and register it
    PlayerController playerController =
        new PlayerController(player,worldSizeInAreas,playerLocation);
    controllers.setPlayerController(playerController);

    // produce Game instance and assign it to ACTIVE
    Game.ACTIVE = new Game(world, controllers);

  }

}
