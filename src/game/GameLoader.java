package game;

import actor.Actor;
import actor.ActorFactory;
import controller.AnimalController;
import controller.PlayerController;
import world.Area;
import world.AreaFactory;

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

  static void newGame(int worldWidth, int worldHeight) {
    if (Game.ACTIVE != null) {
      throw new IllegalStateException("Cannot load Game while another is active, must call " +
          "GameLoader.unload()");
    }

    // produce a map
    Area area = AreaFactory.standardGeneration(worldWidth, worldHeight);

    // populate with animals and a Human for the player to control
    Controllers controllers = new Controllers();

    for (int i = 0; i < 40; i++) {
      String id = (i%2 == 0) ? "DOG" : "CAT";
      Actor actor = ActorFactory.makeActor(id);

      Point point = new Point(Game.RANDOM.nextInt(area.getWidth()),
          Game.RANDOM.nextInt(area.getHeight()));
      area.placePhysical(actor, point.x, point.y);
      controllers.register(new AnimalController(actor, point));
    }

    Actor player = ActorFactory.makeActor("HUMAN");
    int playerX = Game.RANDOM.nextInt(area.getWidth());
    int playerY = Game.RANDOM.nextInt(area.getHeight());
    Point playerLocation = new Point(playerX,playerY);

    area.placePhysical(player, playerX, playerY);

    // assign the Human to a PlayerController and register it
    PlayerController playerController = new PlayerController(player,playerLocation);
    controllers.setPlayerController(playerController);


    // produce Game instance and assign it to ACTIVE
    Game.ACTIVE = new Game(area, controllers);

  }

}
