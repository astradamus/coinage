package game;

import actor.Actor;
import actor.ActorFactory;
import controller.AnimalController;
import controller.PlayerController;
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

  static void newGame(int areaWidth, int areaHeight, int worldWidth, int worldHeight) {
    if (Game.ACTIVE != null) {
      throw new IllegalStateException("Cannot load Game while another is active, must call " +
          "GameLoader.unload()");
    }

    // produce a map
    World world = WorldFactory.standardGeneration(areaWidth, areaHeight, worldWidth, worldHeight);

    // populate with animals and a Human for the player to control
    Controllers controllers = new Controllers();

    for (int i = 0; i < 100; i++) {
      String id = (i%2 == 0) ? "DOG" : "CAT";
      Actor actor = ActorFactory.makeActor(id);

      Point point = new Point(Game.RANDOM.nextInt(world.globalWidth),
          Game.RANDOM.nextInt(world.globalHeight));
      world.globalPlacePhysical(actor, point.x, point.y);
      controllers.register(new AnimalController(actor, point));
    }

    Actor player = ActorFactory.makeActor("HUMAN");
    int playerX = Game.RANDOM.nextInt(world.globalWidth);
    int playerY = Game.RANDOM.nextInt(world.globalHeight);
    Point playerLocation = new Point(playerX,playerY);

    world.globalPlacePhysical(player, playerX, playerY);

    // produce Game instance and assign it to ACTIVE
    Game.ACTIVE = new Game(world, controllers);

    // assign the Human to a PlayerController and register it
    PlayerController playerController = new PlayerController(player,playerLocation);
    controllers.setPlayerController(playerController);

  }

}
