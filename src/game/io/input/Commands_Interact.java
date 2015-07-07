package game.io.input;

import actor.Actor;
import controller.action.PickingUp;
import controller.player.PlayerAgent;
import game.physical.Physical;
import world.Coordinate;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_Interact implements Command {


  PICK_UP {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_P;
    }

    @Override
    public String getControlText() {
      return "P: Pick up item.";
    }

    @Override
    public void execute() {

      PlayerAgent playerAgent = GameInput.getRunningGame().getPlayerAgent();
      Actor playerActor = playerAgent.getActor();

      // Have the player choose what to pick up, then start picking it up.

      final Coordinate playerTarget = GameInput.getPlayerTarget();
      final Integer playerSelection = GameInput.getPlayerSelection();

      final Physical selected = GameInput.getRunningGame().getWorld().getSquare(playerTarget).getAll().get(playerSelection);

      playerAgent.attemptAction(new PickingUp(playerActor, GameInput.getPlayerTarget(), selected));

      GameInput.enterMode(GameMode.EXPLORE);

    }

  }


}