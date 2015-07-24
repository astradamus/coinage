package game.io.input;

import actor.Actor;
import actor.action.Chopping;
import actor.action.PickingUp;
import controller.player.PlayerAgent;
import game.io.display.Event;
import game.io.display.EventLog;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import world.Coordinate;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_Interact implements Command {


  CHOP {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_C;
    }

    @Override
    public String getControlText() {
      return "C: Chop a tree.";
    }

    @Override
    public void execute() {

      PlayerAgent playerAgent = GameInput.getRunningGame().getPlayerAgent();

      final Physical equipped = playerAgent.getActor().getInventory().getWeapon();

      if (!equipped.hasFlag(PhysicalFlag.CAN_CHOP)) {
        EventLog.registerEvent(Event.INVALID_INPUT,
            "You can't chop with your " + equipped.getName() + ".");
        return;
      }

      final Physical selected = GameInput.getRunningGame().getWorld()
          .getSquare(GameInput.getPlayerTarget()).getAll().get(GameInput.getPlayerSelection());

      if (!selected.hasFlag(PhysicalFlag.CHOPPABLE)) {
        EventLog.registerEvent(Event.INVALID_INPUT, "You can't chop " + selected.getName() + ".");
        return;
      }

      EventLog.registerEvent(Event.ALERT_MINOR, "You begin chopping " + selected.getName() +
          "...");
      playerAgent.attemptAction(
          new Chopping(playerAgent.getActor(), GameInput.getPlayerTarget(), selected));

      GameInput.enterMode(GameMode.EXPLORE);
    }
  },


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

      if (selected.hasFlag(PhysicalFlag.IMMOVABLE)) {
        EventLog.registerEvent(Event.INVALID_INPUT, "You can't pick up " + selected.getName() + ".");
        return;
      }

      playerAgent.attemptAction(new PickingUp(playerActor, GameInput.getPlayerTarget(), selected));

      GameInput.enterMode(GameMode.EXPLORE);

    }

  }


}