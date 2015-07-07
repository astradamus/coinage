package game;

import actor.Actor;
import actor.ActorFactory;
import controller.ai.AiActorAgent;
import controller.player.PlayerAgent;
import thing.ThingFactory;
import thing.WeaponTemplates;
import utils.Dimension;
import world.Coordinate;
import world.Square;
import world.World;
import world.WorldFactory;

/**
 *
 */
public class GameBuilder {


  public static Game newGame(Dimension areaSizeInSquares, Dimension worldSizeInAreas) {

    // produce a map
    World world = WorldFactory.standardGeneration(areaSizeInSquares, worldSizeInAreas);

    // instantiate a game with that map
    Game newGame = new Game(world);

    // populate with animals and a Human for the player to control
    GameControllers gameControllers = newGame.getGameControllers();

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
        gameControllers.addController(new AiActorAgent(actor, newGame.getInformer()));
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


    player.getInventory().addItem(ThingFactory.makeThing(WeaponTemplates.WP_CLUB));
    player.getInventory().addItem(ThingFactory.makeThing(WeaponTemplates.WP_SWORD));
    player.getInventory().addItem(ThingFactory.makeThing(WeaponTemplates.WP_AXE));
    player.getInventory().addItem(ThingFactory.makeThing(WeaponTemplates.WP_DAGGER));

    // produce the game instance

    // assign the player agent
    newGame.setPlayerAgent(playerController);

    return newGame;

  }


}