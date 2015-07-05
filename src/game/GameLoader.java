package game;

import actor.Actor;
import actor.ActorFactory;
import actor.ActorTemplate;
import controller.ai.AiActorAgent;
import controller.player.PlayerAgent;
import game.display.GameDisplay;
import game.input.GameInputSwitch;
import thing.ThingTemplate;
import utils.Dimension;
import world.Coordinate;
import world.Square;
import world.World;
import world.WorldFactory;

/**
 *
 */
public class GameLoader {

  static {
    ThingTemplate.loadThings();
    ActorTemplate.loadActors();
  }


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
    GameControllers gameControllers = new GameControllers(world);

    for (int i = 0; i < 600; i++) {
      String id;
      switch (i % 5) {
        case 0: id = "WOLF"; break;
        case 1: id = "COUGAR"; break;
        default: id = "MUSKRAT"; break;
      }
      Actor actor = ActorFactory.makeActor(id);

      if (actor != null) {
        Coordinate randomCoordinate;
        Square square;
        do {
          randomCoordinate = world.makeRandomCoordinate();
          square = world.getSquare(randomCoordinate);
        } while (square.isBlocked());
        square.put(actor);
        actor.setCoordinate(randomCoordinate);
        gameControllers.addController(new AiActorAgent(actor, world));
      }
    }

    Actor player = ActorFactory.makeActor("HUMAN");

    if (player == null) {
      throw new RuntimeException("Failed to instantiate an actor for the player.");
    }

    Coordinate playerStartCoordinate = world.makeRandomCoordinate();
    player.setCoordinate(playerStartCoordinate);

    world.getSquare(playerStartCoordinate).put(player);

    // assign the Human to a PlayerAgent and addController it
    PlayerAgent playerController = new PlayerAgent(player, world);
    playerController.getWorldMapRevealedComponent().setAreaIsRevealed(world.convertToMapCoordinate(playerStartCoordinate));

    gameControllers.addController(playerController);

    // set up the GameInputSwitch
    GameInputSwitch gameInputSwitch = new GameInputSwitch(world);
    gameInputSwitch.setPlayerController(playerController);

    // produce Game instance and assign it to ACTIVE
    final Game activeGame = new Game(world, gameControllers, gameInputSwitch);
    Game.ACTIVE = activeGame;
    GameDisplay.setActiveGame(activeGame);

  }

}