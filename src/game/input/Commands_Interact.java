package game.input;

import actor.Actor;
import controller.action.PickingUp;
import game.Game;
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

      Actor playerActor = Game.getActivePlayerActor();

      // Have the player choose what to pick up, then start picking it up.

      final Coordinate playerTarget = Game.getActiveInputSwitch().getPlayerTarget();
      final Integer playerSelection = Game.getActiveInputSwitch().getPlayerSelection();

      final Physical selected = Game.getActiveWorld().getSquare(playerTarget).getAll().get(playerSelection);

      playerActor.attemptAction(new PickingUp(playerActor,
          Game.getActiveInputSwitch().getPlayerTarget(), selected));

      Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);

    }

  }


}