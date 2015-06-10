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

      if (actor != null) {
        Coordinate randomCoordinate = world.makeRandomCoordinate();
        actor.setCoordinate(randomCoordinate);
        world.put(actor, randomCoordinate);
        gameControllers.register(new AnimalController(actor));
      }
    }

    Actor player = ActorFactory.makeActor("HUMAN");

    if (player == null) {
      throw new RuntimeException("Failed to instantiate an actor for the player.");
    }

    Coordinate playerStartCoordinate = world.makeRandomCoordinate();
    player.setCoordinate(playerStartCoordinate);

    world.put(player, playerStartCoordinate);

    // assign the Human to a PlayerController and register it
    PlayerController playerController = new PlayerController(player,worldSizeInAreas);
    playerController.getWorldMapRevealedComponent().setAreaIsRevealed(playerStartCoordinate);

    gameControllers.register(playerController);

    // set up the GameInputSwitch
    GameInputSwitch gameInputSwitch = new GameInputSwitch();
    gameInputSwitch.setPlayerController(playerController);

    // produce Game instance and assign it to ACTIVE
    Game.ACTIVE = new Game(world, gameControllers, gameInputSwitch);

  }

}
