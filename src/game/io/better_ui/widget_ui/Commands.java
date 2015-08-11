package game.io.better_ui.widget_ui;

import actor.Actor;
import controller.action.PickingUp;
import controller.player.PlayerAgent;
import game.Game;
import game.io.better_ui.Event;
import game.io.better_ui.EventLog;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import world.Coordinate;
import world.World;


/**
 *
 */
public class Commands {

  public static void playerPickUp(Game game, Coordinate playerTarget) {

    final PlayerAgent playerAgent = game.getPlayerAgent();
    final Actor playerActor = playerAgent.getActor();

    if (!playerActor.getCoordinate().getIsAdjacentTo(playerTarget)) {
      EventLog.registerEvent(Event.INVALID_INPUT, "You are too far away.");
      return;
    }

    final World world = game.getWorld();
    final Physical selected = world.getSquare(playerTarget).getAll().get(0);

    if (selected.hasFlag(PhysicalFlag.IMMOVABLE)) {
      EventLog.registerEvent(Event.INVALID_INPUT, "You cannot pick up " + selected.getName() + ".");
      return;
    }

    playerAgent.attemptAction(new PickingUp(playerActor, playerTarget, selected));
  }

}
